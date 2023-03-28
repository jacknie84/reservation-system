package com.jacknie.reservation.jpa

import java.time.Instant

interface ReservationCustomRepository {

    fun existsDuplicate(excludedId: Long, facilityId: Long, start: Instant, end: Instant): Boolean

    fun findUsages(contractId: Long): List<Reservation>
}