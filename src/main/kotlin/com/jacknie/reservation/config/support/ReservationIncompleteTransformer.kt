package com.jacknie.reservation.config.support

import com.jacknie.reservation.facade.ContractFacade
import com.jacknie.reservation.facade.ReservationFacade
import com.jacknie.reservation.jpa.Reservation
import org.springframework.integration.core.GenericTransformer

class ReservationIncompleteTransformer(
    private val reservationFacade: ReservationFacade,
    private val contractFacade: ContractFacade
): GenericTransformer<Reservation, Reservation> {

    override fun transform(reservation: Reservation): Reservation = reservation.apply {
        incompleteReasons.clear()
        if (isAlreadyExists(this)) {
            incompleteReasons += Reservation.IncompleteReason.ALREADY_EXISTS
        }
        if (isExceededUsageLimit(this)) {
            incompleteReasons += Reservation.IncompleteReason.EXCEEDED_USAGE_LIMIT
        }
        val code = if (incompleteReasons.isEmpty()) Reservation.Status.Code.RESERVED else Reservation.Status.Code.INCOMPLETE
        status = Reservation.Status(code)
    }

    private fun isAlreadyExists(reservation: Reservation): Boolean {
        return reservationFacade.existsReservation(reservation)
    }

    private fun isExceededUsageLimit(reservation: Reservation): Boolean {
        val usageLimit = reservation.run { contractFacade.getUsageLimit(contractId) }
        val usageTime = reservation.run { reservationFacade.getTotalUsageTime(reservation) }
        return usageTime > usageLimit
    }
}