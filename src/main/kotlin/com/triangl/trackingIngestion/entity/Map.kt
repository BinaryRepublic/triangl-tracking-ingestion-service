package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import java.util.*
import kotlin.collections.ArrayList

@javax.persistence.Entity
@Entity
class Map (
    @Id
    var id: String? = null,

    var name: String? = null,

    var svgPath: String? = null,

    @Index
    var size: Coordinate? = null,

    @Index
    var router: ArrayList<Router>? = null
) {
    init {
        this.id = UUID.randomUUID().toString()
        this.router = ArrayList()
    }
}