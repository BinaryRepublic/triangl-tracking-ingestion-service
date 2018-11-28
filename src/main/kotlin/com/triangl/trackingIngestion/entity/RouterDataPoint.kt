package com.triangl.trackingIngestion.entity

import java.util.*

class RouterDataPoint (
    var id: String? = null,

    var router: Router? = null,

    var associatedAP: String? = null,

    var signalStrength: Int? = null,

    var timeOfFlight: Long? = null,

    var timestamp: String? = null
) {
    init {
        id = UUID.randomUUID().toString()
    }
}