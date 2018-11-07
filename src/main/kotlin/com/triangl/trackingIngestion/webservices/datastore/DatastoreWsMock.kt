package com.triangl.trackingIngestion.webservices.datastore

import com.googlecode.objectify.Key
import com.triangl.trackingIngestion.dto.RouterLastSeenDto
import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.entity.Map
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
@Profile("test")
class DatastoreWsMock: DatastoreWs {
    override fun getRouterLastSeenList(): List<RouterLastSeenDto> {
        return listOf(RouterLastSeenDto("",""))
    }

    val router1 = Router(id = "Router1", location = Coordinate(x = 1f, y = 2f))
    val router2 = Router(id = "Router2", location = Coordinate(x = 5f, y = 2f))
    val router3 = Router(id = "Router3", location = Coordinate(x = 3f, y = 5f))
    val now = LocalDateTime.now().toString()

    override fun saveTrackingPoint(trackingPoint: TrackingPoint): Key<TrackingPoint> = Key.create(TrackingPoint::class.java, UUID.randomUUID().toString())

    override fun getTrackingPointByKey(key: Key<TrackingPoint>): TrackingPoint {
        val routerDataPoint1 = RouterDataPoint(router = router1, signalStrength = 100, timestamp = now)
        val routerDataPoint2 = RouterDataPoint(router = router1, signalStrength = 100, timestamp = now)
        val routerDataPoint3 = RouterDataPoint(router = router1, signalStrength = 100, timestamp = now)
        val routerDataList = arrayListOf(routerDataPoint1, routerDataPoint2, routerDataPoint3)

        return TrackingPoint(routerDataPoints = routerDataList, deviceId = "Device1", location = Coordinate(x = 3f, y = 5f))
    }

    override fun getCustomerByRouterId(routerId: String): Customer? {
        val map = Map(id = "ID1", name = "Map1", svgPath = "/dir", size =  Coordinate(x = 10f, y = 10f), router = arrayListOf(router1, router2, router3))

        return Customer("Customer").apply { maps = listOf(map) }
    }
}