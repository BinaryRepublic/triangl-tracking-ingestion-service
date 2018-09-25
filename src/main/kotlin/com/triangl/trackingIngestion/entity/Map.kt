package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import java.util.*
import javax.validation.constraints.NotNull

@javax.persistence.Entity
@Entity
class Map {
    @Id
    var id: String? = null

    @NotNull
    var name: String? = null

    @NotNull
    var svgPath: String? = null

    @Index
    @NotNull
    var size: Coordinate? = null

    @Index
    @NotNull
    var router: List<Router>? = null

    @Suppress("unused")
    constructor()

    constructor(name: String, svgPath: String, size: Coordinate, routes: List<Router>) {
        this.id = UUID.randomUUID().toString()
        this.name = name
        this.svgPath = svgPath
        this.size = size
        this.router = routes
    }
}