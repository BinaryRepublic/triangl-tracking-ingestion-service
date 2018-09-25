package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.time.Instant
import java.util.*

@Entity
class DatapointGroup (
        startInstant: String,
        var deviceId: String
) {
    @Id
    var id: String = UUID.randomUUID().toString()

    var startInstant = Instant.parse(startInstant)
                        .minusSeconds(3)
                        .toString()

    var endInstant = Instant.parse(startInstant)
                         .plusSeconds(3)
                         .toString()

    var dataPoints: ArrayList<InputDataPoint>? = null

    var timeoutInstant = Instant.parse(startInstant)
                         .plusSeconds(30)
                         .toString()
}