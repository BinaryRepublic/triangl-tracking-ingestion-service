package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import java.time.Instant
import java.util.*

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
        this.deleted = false
        this.createdAt = Instant.now().toString()
        this.lastUpdatedAt = Instant.now().toString()
    }

    fun toRoutersHashmap(): HashMap<String, Router> {
        val hashMap = HashMap<String, Router>()

        for (map in maps!!) {
            for (router in map.router!!) {
                hashMap[router.id!!] = router
            }
        }

        return hashMap
    }
}