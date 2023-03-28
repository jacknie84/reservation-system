package com.jacknie.reservation.test.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

fun <T> MockHttpServletRequestBuilder.json(body: T, objectMapper: ObjectMapper): MockHttpServletRequestBuilder {
    val content = objectMapper.writeValueAsString(body)
    return content(content).contentType(MediaType.APPLICATION_JSON)
}