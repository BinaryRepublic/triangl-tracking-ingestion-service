package com.triangl.trackingIngestion

import com.googlecode.objectify.ObjectifyFilter
import com.googlecode.objectify.ObjectifyService
import com.triangl.trackingIngestion.entity.Coordinate
import com.triangl.trackingIngestion.entity.Router
import com.triangl.trackingIngestion.entity.RouterDataPoint
import com.triangl.trackingIngestion.entity.TrackingPoint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@Component
class ObjectifyWebFilter : ObjectifyFilter()

@SpringBootApplication
class TrackingIngestionApplication

fun main(args: Array<String>) {

    ObjectifyService.init()
    ObjectifyService.register(RouterDataPoint::class.java)
    ObjectifyService.register(TrackingPoint::class.java)
    ObjectifyService.register(Router::class.java)
    ObjectifyService.register(Coordinate::class.java)

    runApplication<TrackingIngestionApplication>(*args)
}
