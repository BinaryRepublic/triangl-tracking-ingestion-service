package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.time.Instant
import java.util.*

@Entity
class TrackingPoint (
    @Id
    var id: String? = null,

    var routerDataPoints: List<RouterDataPoint>? = null,

    var deviceId: String? = null,

    var location: Coordinate? = null,

    var timestamp: String? = null,

    var lastUpdatedAt: String? = null,

    var createdAt: String? = null
) {
    init {
        this.id = UUID.randomUUID().toString()
        this.lastUpdatedAt = Instant.now().toString()
        this.createdAt = Instant.now().toString()
    }
}