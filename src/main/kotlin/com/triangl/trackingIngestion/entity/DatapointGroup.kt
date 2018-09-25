package com.triangl.trackingIngestion.entity

import java.time.Instant
import java.util.*

class DatapointGroup (
        startInstant: String,
        var deviceId: String
) {
    var startInstant = Instant.parse(startInstant)
                        .minusSeconds(3)
                        .toString()

    var endInstant = Instant.parse(startInstant)
                         .plusSeconds(3)
                         .toString()

    var dataPoints = ArrayList<InputDataPoint>()

    var timeoutInstant = Instant.parse(startInstant)
                         .plusSeconds(30)
                         .toString()
}