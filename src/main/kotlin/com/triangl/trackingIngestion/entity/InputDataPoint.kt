package com.triangl.trackingIngestion.entity

class InputDataPoint (
    var routerId: String,

    var deviceId: String,

    var timestamp: String,

    var signalStrength: Int? = null,

    var timeOfFlight: Long? = null
)