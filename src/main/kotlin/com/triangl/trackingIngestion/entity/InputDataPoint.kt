package com.triangl.trackingIngestion.entity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InputDataPoint (
    var routerId: String,

    var deviceId: String,

    timestampString: String,

    var signalStrength: Int,

    var timeOfFlight: Long? = null
) {
    var timestamp: LocalDateTime = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}