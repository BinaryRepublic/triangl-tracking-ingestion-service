package com.triangl.trackingIngestion

import com.googlecode.objectify.ObjectifyFilter
import com.googlecode.objectify.ObjectifyService
import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.entity.Map
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("production")
class ObjectifyWebFilter : ObjectifyFilter()

@SpringBootApplication
class TrackingIngestionApplication

fun main(args: Array<String>) {

    ObjectifyService.init()
    ObjectifyService.register(Customer::class.java)
    ObjectifyService.register(TrackingPoint::class.java)

    runApplication<TrackingIngestionApplication>(*args)
}
