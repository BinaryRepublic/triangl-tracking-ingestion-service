package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import java.util.*
import javax.persistence.Entity
import kotlin.collections.ArrayList

@Entity
class Map (
    var id: String? = null,

    var name: String? = null,

    var svgPath: String? = null,

    var size: Coordinate? = null,

    var router: ArrayList<Router>? = null
) {
    init {
        this.id = UUID.randomUUID().toString()
        this.router = ArrayList()
    }
}