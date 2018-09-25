package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import javax.validation.constraints.NotNull

@Entity
class Router (
    @Id
    var id: String? = null
) {

    @NotNull
    var location: Coordinate? = null

}