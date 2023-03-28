package com.jacknie.reservation.web

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.Instant

data class CreateReservation(

    @field:NotBlank
    var summary: String? = null,

    @field:NotNull
    var start: Instant? = null,

    @field:NotNull
    var end: Instant? = null,

    @field:NotNull
    @field:Positive
    var contractId: Long? = null,

    @field:NotNull
    @field:Positive
    var facilityId: Long? = null,
)
