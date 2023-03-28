package com.jacknie.reservation.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository: JpaRepository<Reservation, Long>, ReservationCustomRepository {
}