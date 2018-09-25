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
    fun `should return a list of customers`() {

        val routerIds = listOf("Router1", "Router2", "Router3")
        val deviceId = "Device1"
        val signalStrengths = listOf(255, 100, 50)
        val now = Instant.now().toString()

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"deviceId\": \"$deviceId\", \"routerId\": \"${routerIds[0]}\", \"signalStrength\": \"${signalStrengths[0]}\", \"timestamp\": \"$now\" }")
                .post("/tracking")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value())

        Thread.sleep(1000)

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"deviceId\": \"$deviceId\", \"routerId\": \"${routerIds[1]}\", \"signalStrength\": \"${signalStrengths[1]}\", \"timestamp\": \"$now\" }")
                .post("/tracking")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value())

        Thread.sleep(1000)

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{ \"deviceId\": \"$deviceId\", \"routerId\": \"${routerIds[2]}\", \"signalStrength\": \"${signalStrengths[2]}\", \"timestamp\": \"$now\" }")
                .post("/tracking")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value())

        Thread.sleep(60000)
    }
}