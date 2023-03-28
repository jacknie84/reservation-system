package com.jacknie.reservation.test.jpa

import com.jacknie.reservation.jpa.Reservation
import com.jacknie.reservation.jpa.ReservationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant

@DataJpaTest
class ReservationEntityTest {

    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @DisplayName("예약 생성 테스트")
    @Test
    fun createTest() {

        val entity = reservationRepository.save(Reservation(
            status = Reservation.Status(Reservation.Status.Code.ACCEPTED),
            start = Instant.now(),
            end = Instant.now()
        ))
        val exists = reservationRepository.findById(entity.id!!).orElseThrow { fail() }
        println(exists)
        assertNotNull(exists)
    }
}