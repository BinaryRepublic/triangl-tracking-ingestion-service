package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Id
import java.util.*
import javax.persistence.Entity

@Entity
class RouterDataPoint (
    var id: String? = null,

    var router: Router? = null,

    var signalStrength: Int? = null,

    var timeOfFlight: Long? = null,

    var timestamp: String? = null
) {
    init {
        id = UUID.randomUUID().toString()
    }
}