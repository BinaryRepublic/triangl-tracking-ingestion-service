package com.triangl.trackingIngestion

import com.googlecode.objectify.ObjectifyFilter
import com.googlecode.objectify.ObjectifyService
import com.triangl.trackingIngestion.entity.Customer
import com.triangl.trackingIngestion.entity.TrackingPoint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.stereotype.Component


@Component
@Profile("production")
class ObjectifyWebFilter : ObjectifyFilter()

@SpringBootApplication
@PropertySources(PropertySource("classpath:application.properties"), PropertySource("classpath:auth0.properties"))
class TrackingIngestionApplication

fun main(args: Array<String>) {

    ObjectifyService.init()
    ObjectifyService.register(Customer::class.java)
    ObjectifyService.register(TrackingPoint::class.java)

    runApplication<TrackingIngestionApplication>(*args)
}