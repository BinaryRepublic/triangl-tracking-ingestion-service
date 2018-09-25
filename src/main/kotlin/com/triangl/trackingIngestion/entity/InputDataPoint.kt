package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import javax.validation.constraints.NotNull

@Entity
class InputDataPoint {
    @Id
    @NotNull
    var id: String? = null

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