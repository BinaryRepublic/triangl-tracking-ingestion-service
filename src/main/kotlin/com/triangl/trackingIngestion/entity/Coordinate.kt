package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import java.util.*
import javax.validation.constraints.NotNull

@Entity
class Coordinate {
    @Id
    @NotNull
    var id: String? = null

    @NotNull
    var x: Float? = null

    @NotNull
    var y: Float? = null

    @Suppress("unused")
    constructor()

    constructor(x: Float, y: Float) {
        this.id = UUID.randomUUID().toString()
        this.x = x
        this.y = y
    }
}