package com.triangl.trackingIngestion.entity

import java.time.Instant
import java.util.*

class DatapointGroup (
        startInstantString: String,
        var deviceId: String
) {
    var startInstant = Instant.parse(startInstantString)
                        .minusSeconds(3)
                        .toString()

    var endInstant = Instant.parse(startInstantString)
                         .plusSeconds(3)
                         .toString()

    var dataPoints = ArrayList<InputDataPoint>()

    var timeoutInstant = Instant.parse(startInstantString)
                         .plusSeconds(30)
                         .toString()
}