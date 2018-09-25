package com.triangl.trackingIngestion.entity

import javax.validation.constraints.NotNull

class InputDataPoint {
    @NotNull
    var routerId: String? = null

    @NotNull
    var deviceId: String? = null

    @NotNull
    var signalStrength: Int? = null

    @NotNull
    var timeOfFlight: Long? = null

    @NotNull
    var timestamp: String? = null
}