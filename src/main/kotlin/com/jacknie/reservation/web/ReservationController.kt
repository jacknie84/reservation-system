package com.jacknie.reservation.web

import com.jacknie.reservation.jpa.Reservation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservations")
class ReservationController(private val reservationService: ReservationService) {

    @PostMapping
    fun createReservation(@RequestBody @Valid create: CreateReservation): ResponseEntity<*> {
        val reservation = reservationService.createReservation(create)
        return ResponseEntity.accepted().body(reservation)
    }
}