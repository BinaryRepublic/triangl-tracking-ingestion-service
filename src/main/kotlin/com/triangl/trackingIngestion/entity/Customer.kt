package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import java.time.Instant
import java.util.*
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

@javax.persistence.Entity
@Entity
class Customer {
    @Id
    var id: String? = null

    @NotNull
    var name: String? = null

    @Index
    @NotNull
    var maps: List<Map>? = null

    @NotNull
    var deleted: Boolean? = null

    @NotNull
    var lastUpdatedAt: String? = null

    @NotNull
    var createdAt: String? = null

    @Suppress("unused")
    constructor()

    constructor(name: String) {
        this.id = UUID.randomUUID().toString()
        this.name = name
        this.maps = ArrayList()
        this.deleted = false
        this.createdAt = Instant.now().toString()
        this.lastUpdatedAt = Instant.now().toString()
    }
}