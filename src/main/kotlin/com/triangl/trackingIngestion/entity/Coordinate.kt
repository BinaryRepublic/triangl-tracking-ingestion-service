package com.triangl.trackingIngestion.entity

import java.util.*
import javax.persistence.Entity

@Entity
class Coordinate (
    var id: String? = null,

    var x: Float? = null,

    var y: Float? = null
) {
    init {
        this.id = UUID.randomUUID().toString()
    }
}