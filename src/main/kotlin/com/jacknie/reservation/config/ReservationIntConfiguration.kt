package com.jacknie.reservation.config

import com.jacknie.reservation.config.support.ReservationInProgressTransformer
import com.jacknie.reservation.config.support.ReservationIncompleteTransformer
import com.jacknie.reservation.facade.ContractFacade
import com.jacknie.reservation.facade.ReservationFacade
import com.jacknie.reservation.jpa.Reservation
import com.jacknie.reservation.jpa.inprogress.ReservationInProgress
import com.jacknie.reservation.jpa.inprogress.ReservationInProgressRepository
import jakarta.persistence.EntityManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.integration.dsl.*
import org.springframework.integration.jpa.dsl.Jpa
import java.util.concurrent.TimeUnit

@Configuration
class ReservationIntConfiguration(
    private val entityManagerFactory: EntityManagerFactory,
    private val reservationFacade: ReservationFacade,
    private val contractFacade: ContractFacade,
    private val inProgressRepository: ReservationInProgressRepository,
) {

    @Bean
    fun acceptedFlow(): IntegrationFlow {
        val inboundAdapter = Jpa.inboundAdapter(entityManagerFactory)
            .jpaQuery("from Reservation where status.code = 'ACCEPTED' order by status.date")
        val pollingSpec: SourcePollingChannelAdapterSpec.() -> Unit = {
            poller { it.fixedDelay(1, TimeUnit.SECONDS).transactional() }
        }
        return integrationFlow(inboundAdapter, pollingSpec) {
            transform<List<Reservation>> {
                it.map { entity -> entity.apply { status = Reservation.Status(Reservation.Status.Code.IN_PROGRESS) } }
            }
            handle(Jpa.updatingGateway(entityManagerFactory).entityClass(Reservation::class.java))
            transform(ReservationInProgressTransformer(inProgressRepository))
            handle(Jpa.updatingGateway(entityManagerFactory).entityClass(ReservationInProgress::class.java))
            channel("nullChannel")
        }
    }

    @Bean
    fun inProgressFlow(): IntegrationFlow {
        val executor = SimpleAsyncTaskExecutor("in-progress-exec-")
        val inboundAdapter = Jpa.inboundAdapter(entityManagerFactory)
            .jpaQuery("from ReservationInProgress order by createdDate")
            .deleteAfterPoll(true)
            .deleteInBatch(true)
        val pollingSpec: SourcePollingChannelAdapterSpec.() -> Unit = { poller { it.fixedDelay(1, TimeUnit.SECONDS).transactional() } }
        return integrationFlow(inboundAdapter, pollingSpec) {
            split()
            channel(MessageChannels.executor("inProgressExecuteChannel", executor))
        }
    }

    @Bean
    fun inProgressExecuteFlow(): IntegrationFlow = integrationFlow("inProgressExecuteChannel") {
        split<List<ReservationInProgress>> {
            it.flatMap { inProgress -> inProgress.reservations.sortedBy { reservation -> reservation.status.date } }
        }
        log()
        transform(ReservationIncompleteTransformer(reservationFacade, contractFacade))
        handle(Jpa.updatingGateway(entityManagerFactory).entityClass(Reservation::class.java)) { transactional() }
        channel("nullChannel")
    }
}