package com.triangl.trackingIngestion.entity

import java.time.LocalDateTime
import java.util.*

class DatapointGroup (
        startInstant: LocalDateTime,
        var deviceId: String
) {
    var startInstant: LocalDateTime = startInstant.minusSeconds(3)

    var endInstant: LocalDateTime = startInstant.plusSeconds(3)

    var dataPoints = ArrayList<InputDataPoint>()

    var timeoutInstant: LocalDateTime = startInstant.plusSeconds(30)
}