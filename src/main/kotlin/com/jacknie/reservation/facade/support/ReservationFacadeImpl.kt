package com.jacknie.reservation.facade.support

import com.jacknie.reservation.facade.ReservationFacade
import com.jacknie.reservation.jpa.Reservation
import com.jacknie.reservation.jpa.ReservationRepository
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class ReservationFacadeImpl(private val reservationRepository: ReservationRepository): ReservationFacade {

    override fun existsReservation(reservation: Reservation): Boolean {
        return reservation.run { reservationRepository.existsDuplicate(id!!, facilityId,start, end) }
    }

    override fun getTotalUsageTime(reservation: Reservation): Duration {
        val entities = reservation.run { reservationRepository.findUsages(contractId) } + reservation
        return entities
            .map { Duration.between(it.start, it.end) }
            .filterNot { it.isNegative }
            .reduce { acc, duration -> acc.plus(duration) }
    }
}