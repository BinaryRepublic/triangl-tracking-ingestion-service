package com.triangl.trackingIngestion

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.time.Instant

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomerIntegrationTest {

    @Value("\${local.server.port}")
    private val serverPort: Int = 0

    @Before
    fun setUp() {
        RestAssured.port = serverPort
    }

    @Test
    fun `should accept an InputDataPoint`() {

        val routerId = "Router1"
        val deviceId = "Device1"
        val signalStrength = 255
        val now = Instant.now().toString()

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"deviceId\": \"$deviceId\", \"routerId\": \"$routerId\", \"signalStrength\": \"$signalStrength\", \"timestamp\": \"$now\" }")
                .post("/tracking")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.NO_CONTENT.value())

    }

    @Test
    fun `should accept a list of InputDataPoints`() {

        val routerId = "Router1"
        val deviceId = "Device1"
        val signalStrength = 255
        val now = Instant.now().toString()

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("[{ \"deviceId\": \"$deviceId\", \"routerId\": \"$routerId\", \"signalStrength\": \"$signalStrength\", \"timestamp\": \"$now\" }," +
                            " { \"deviceId\": \"$deviceId\", \"routerId\": \"$routerId\", \"signalStrength\": \"$signalStrength\", \"timestamp\": \"$now\" }]")
                .post("/tracking/multiple")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.NO_CONTENT.value())
        Thread.sleep(40000)
    }

    @Test
    fun `should return 404 because wrong input format`() {

        val routerId = "Router1"

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"routerId\": \"$routerId\" }")
                .post("/tracking")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value())
    }
}