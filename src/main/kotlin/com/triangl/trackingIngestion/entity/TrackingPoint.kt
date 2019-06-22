package com.triangl.trackingIngestion.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.Index
import org.springframework.security.crypto.bcrypt.BCrypt
import java.time.Instant
import java.util.*

@Entity
class TrackingPoint (
    @Id
    var id: String? = null,

    @Index
    var routerDataPoints: List<RouterDataPoint>,

    @Index
    var deviceId: String? = null,

    @Index
    var location: Coordinate? = null,

    @Index
    var timestamp: String? = null,

    @Index
    var lastUpdatedAt: String? = null,

    @Index
    var createdAt: String? = null
) {
    init {
        this.id = this.id ?: UUID.randomUUID().toString()
        this.lastUpdatedAt = this.lastUpdatedAt ?: Instant.now().toString()
        this.createdAt = this.createdAt ?: Instant.now().toString()
    }

    fun fillMissingRouterCoordinates(routerHashMap: HashMap<String, Router>) {
        for (routerDataPoint in routerDataPoints) {
            routerDataPoint.router!!.location = routerHashMap[routerDataPoint.router!!.id]!!.location
        }
    }

    fun setLocationAndTimestampFromRouterDataPoint(routerDataPoint: RouterDataPoint) {
        location = Coordinate(
            x = routerDataPoint.router!!.location!!.x,
            y = routerDataPoint.router!!.location!!.y
        )
        timestamp = routerDataPoint.timestamp
    }

    fun hashMacAddress(salt: String, pepper: String) {
        val notBeingHashed = this.deviceId!!.substring(0,8)     // this is the manufacturer identifier
        val beingHashed = this.deviceId!!.substring(8)

        this.deviceId = notBeingHashed + BCrypt.hashpw(beingHashed + pepper, salt)
    }
}