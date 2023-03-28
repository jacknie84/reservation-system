package com.jacknie.reservation.config.support

import com.jacknie.reservation.jpa.Reservation
import com.jacknie.reservation.jpa.inprogress.ReservationInProgress
import com.jacknie.reservation.jpa.inprogress.ReservationInProgressRepository
import org.springframework.integration.core.GenericTransformer

class ReservationInProgressTransformer(
    private val inProgressRepository: ReservationInProgressRepository,
): GenericTransformer<List<Reservation>, List<ReservationInProgress>> {

    override fun transform(reservations: List<Reservation>?): List<ReservationInProgress> {
        return reservations?.groupBy { entity -> entity.facilityId }
            ?.map { (facilityId, reservations) ->
                inProgressRepository.findById(facilityId)
                    .map { inProgress -> inProgress.apply { this.reservations += reservations } }
                    .orElseGet { ReservationInProgress(facilityId, reservations.toMutableList()) }
            }
            ?: emptyList()
    }
}