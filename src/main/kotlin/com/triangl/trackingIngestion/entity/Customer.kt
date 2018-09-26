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
class Customer (
    @Id
    var id: String? = null,

    var name: String? = null,

    @Index
    var maps: List<Map>? = null,

    var deleted: Boolean? = null,

    var lastUpdatedAt: String? = null,

    var createdAt: String? = null
) {
    init {
        this.id = UUID.randomUUID().toString()
        this.maps = ArrayList()
        this.deleted = false
        this.createdAt = Instant.now().toString()
        this.lastUpdatedAt = Instant.now().toString()
    }
}