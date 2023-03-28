package com.jacknie.reservation.web

import com.jacknie.reservation.jpa.Reservation

interface ReservationService {

    fun createReservation(reservation: CreateReservation): Reservation
}