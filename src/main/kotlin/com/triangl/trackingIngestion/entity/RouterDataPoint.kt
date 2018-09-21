package com.triangl.trackingIngestion.entity

import java.time.Instant
import javax.validation.constraints.NotNull
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id

@Entity
class RouterDataPoint {
    @Id
    @NotNull
    var id: String? = null

    @NotNull
    var router: Router? = null

    @NotNull
    var signalStrength: Float? = null

    @NotNull
    var timeOfFlight: Long? = null

    @NotNull
    var timestamp: Instant? = null
}