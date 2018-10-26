package com.triangl.trackingIngestion.entity

import java.time.LocalDateTime
import java.util.*

class DatapointGroup (
        initialInstant: LocalDateTime,
        var deviceId: String
) {
    var startInstant: LocalDateTime = initialInstant.minusSeconds(3)

    var endInstant: LocalDateTime = initialInstant.plusSeconds(3)

    var dataPoints = ArrayList<InputDataPoint>()

    var timeoutInstant: LocalDateTime = initialInstant.plusSeconds(30)
}