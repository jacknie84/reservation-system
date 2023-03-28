package com.jacknie.reservation.test.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.jacknie.reservation.web.CreateReservation
import com.jacknie.reservation.web.ReservationController
import com.jacknie.reservation.web.ReservationService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@WebMvcTest(ReservationController::class)
class ReservationControllerTest {

    @Autowired
    lateinit var mvc: MockMvc
    
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var reservationService: ReservationService

    @DisplayName("정상 예약 등록 테스트")
    @Test
    fun createReservation_test() {
        val reservation = CreateReservation(summary = "예약 테스트", start = Instant.now(), end = Instant.now())
        val request = post("/reservations").json(reservation, objectMapper)
        mvc.perform(request).andExpect(status().isAccepted).andDo(print())
    }

    @DisplayName("예약 유효성 검사 실패 테스트")
    @Test
    fun createReservation_no_summary_test() {
        val reservation = CreateReservation()
        val request = post("/reservations").json(reservation, objectMapper)
        mvc.perform(request).andExpect(status().isBadRequest).andDo(print())
    }
}