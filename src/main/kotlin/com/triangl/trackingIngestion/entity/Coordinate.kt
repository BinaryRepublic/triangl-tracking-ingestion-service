package com.triangl.trackingIngestion.entity

import java.time.Instant
import java.util.*

class Coordinate (
    var id: String? = null,

    var x: Float? = null,

    var y: Float? = null,

    var lastUpdatedAt: String? = null,

    var createdAt: String? = null
) {
    init {
        this.id = UUID.randomUUID().toString()
        this.lastUpdatedAt = Instant.now().toString()
        this.createdAt = Instant.now().toString()
    }
}