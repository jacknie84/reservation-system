package com.jacknie.reservation.web

import com.jacknie.reservation.jpa.Reservation
import com.jacknie.reservation.jpa.ReservationRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ReservationServiceImpl(private val reservationRepository: ReservationRepository): ReservationService {

    override fun createReservation(reservation: CreateReservation): Reservation {
        val entity = Reservation(
            summary = reservation.summary,
            status = Reservation.Status(Reservation.Status.Code.ACCEPTED),
            start = reservation.start!!,
            end = reservation.end!!,
            contractId = reservation.contractId!!,
            facilityId = reservation.facilityId!!,
        )
        return reservationRepository.save(entity)
    }
}