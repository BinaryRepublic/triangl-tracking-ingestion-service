package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import javax.validation.constraints.NotNull

@Entity
class TrackingPoint {
    @Id
    @NotNull
    var id: String? = null

    @NotNull
    var routerDataPoints: List<RouterDataPoint>? = null

    @NotNull
    var deviceId: String? = null

    @NotNull
    var location: Coordinate? = null

    @NotNull
    var deleted: Boolean? = null

    @NotNull
    var lastUpdatedAt: String? = null

    @NotNull
    var createdAt: String? = null
}