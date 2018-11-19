package com.triangl.trackingIngestion.entity

class Map (
    var id: String? = null,

    var name: String? = null,

    var svgPath: String? = null,

    var size: Coordinate? = null,

    var areas: List<Area>? = null,

    var router: ArrayList<Router>? = null,

    var lastUpdatedAt: String? = null,

    var createdAt: String? = null
)