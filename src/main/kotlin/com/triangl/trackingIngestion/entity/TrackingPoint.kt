package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.time.Instant
import java.util.*
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

    @Suppress("unused")
    constructor()

    constructor(routerDataPoints: List<RouterDataPoint>, deviceId: String, x: Float, y: Float) {
        this.id = UUID.randomUUID().toString()
        this.routerDataPoints = routerDataPoints
        this.deviceId = deviceId
        this.location = Coordinate(x,y)
        this.deleted = false
        this.lastUpdatedAt = Instant.now().toString()
        this.createdAt = Instant.now().toString()
    }
}