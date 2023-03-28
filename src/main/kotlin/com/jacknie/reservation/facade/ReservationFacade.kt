package com.jacknie.reservation.facade

import com.jacknie.reservation.jpa.Reservation
import java.time.Duration

interface ReservationFacade {

    fun existsReservation(reservation: Reservation): Boolean

    fun getTotalUsageTime(reservation: Reservation): Duration
}