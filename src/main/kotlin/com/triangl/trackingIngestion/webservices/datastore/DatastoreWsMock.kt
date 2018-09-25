package com.triangl.trackingIngestion.webservices.datastore

import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.entity.Map
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class DatastoreWsMock: DatastoreWs {
    override fun saveTrackingPoint(trackingPoint: TrackingPoint) { }

    override fun getRoutersById(IDList: List<String>): Customer {
        val router1 = Router("Router1").apply { location = Coordinate(1f,2f) }
        val router2 = Router("Router2").apply { location = Coordinate(5f,2f) }
        val router3 = Router("Router3").apply { location = Coordinate(3f,5f) }

        val map = Map("Map1","/dir", Coordinate(10f,10f), listOf(router1, router2, router3))

        return Customer("Customer").apply { maps = listOf(map) }
    }
}