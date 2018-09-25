package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.util.*

@Entity
class RouterDataPoint (

    var router: Router? = null,

    var signalStrength: Int? = null,

    var timeOfFlight: Long? = null,

    var timestamp: String? = null

) {
    @Id
    var id: String? = null

    init {
        id = UUID.randomUUID().toString()
    }
}