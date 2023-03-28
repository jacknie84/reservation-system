package com.jacknie.reservation.jpa

import com.jacknie.reservation.jpa.QReservation.reservation
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.time.Instant

@Suppress("unused")
class ReservationCustomRepositoryImpl: QuerydslRepositorySupport(Reservation::class.java), ReservationCustomRepository {

    companion object {
        val USAGE_STATUS_CODES = setOf(Reservation.Status.Code.RESERVED, Reservation.Status.Code.CONFIRMED)
    }

    override fun existsDuplicate(excludedId: Long, facilityId: Long, start: Instant, end: Instant): Boolean {
        val id = from(reservation).select(reservation.id)
            .where(
                reservation.id.ne(excludedId),
                reservation.status.code.`in`(USAGE_STATUS_CODES),
                reservation.facilityId.eq(facilityId),
                reservation.start.lt(end),
                reservation.end.gt(start)
            )
            .fetchFirst()
        return id != null
    }

    override fun findUsages(contractId: Long): List<Reservation> {
        return from(reservation)
            .where(
                reservation.contractId.eq(contractId),
                reservation.status.code.`in`(USAGE_STATUS_CODES),
            )
            .fetch()
    }
}