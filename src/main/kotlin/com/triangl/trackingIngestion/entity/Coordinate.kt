package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.util.*
import javax.validation.constraints.NotNull

@Entity
class Coordinate (
    @Id
    var id: String? = null,

    var x: Float? = null,

    var y: Float? = null
) {
    init {
        this.id = UUID.randomUUID().toString()
    }
}