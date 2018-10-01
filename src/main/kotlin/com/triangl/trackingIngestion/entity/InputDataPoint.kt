package com.triangl.trackingIngestion.entity

class InputDataPoint (
    var routerId: String,

    var deviceId: String,

    var timestamp: String,

    var signalStrength: Int,

    var timeOfFlight: Long? = null
)