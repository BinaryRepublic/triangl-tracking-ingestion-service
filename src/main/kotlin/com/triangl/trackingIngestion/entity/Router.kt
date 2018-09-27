package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import javax.validation.constraints.NotNull

class Router (
    var id: String? = null,

    var location: Coordinate? = null
)