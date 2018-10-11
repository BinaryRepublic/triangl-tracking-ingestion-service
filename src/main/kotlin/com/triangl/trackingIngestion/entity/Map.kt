package com.triangl.trackingIngestion.entity

import javax.persistence.Entity

@Entity
class Map (
    var id: String? = null,

    var name: String? = null,

    var svgPath: String? = null,

    var size: Coordinate? = null,

    var router: ArrayList<Router>? = null
)