package com.jacknie.reservation.jpa.inprogress

import org.springframework.data.jpa.repository.JpaRepository

interface ReservationInProgressRepository: JpaRepository<ReservationInProgress, Long> {
}