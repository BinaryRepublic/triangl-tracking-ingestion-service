package com.triangl.trackingIngestion.entity

import java.time.LocalDateTime

class DatapointGroup (
    firstInputDataPoint: InputDataPoint
) {
    var startInstant: LocalDateTime = firstInputDataPoint.timestamp.minusSeconds(3)

    var endInstant: LocalDateTime = firstInputDataPoint.timestamp.plusSeconds(3)

    var deviceId = firstInputDataPoint.deviceId

    var dataPoints = arrayListOf(firstInputDataPoint)

    var timeoutInstant: LocalDateTime = firstInputDataPoint.timestamp.plusSeconds(30)
}