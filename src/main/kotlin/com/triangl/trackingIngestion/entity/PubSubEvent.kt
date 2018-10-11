package com.triangl.trackingIngestion.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

class PubSubEvent (
        dataObject: TrackingPoint,
        operation: String,
        additional: HashMap<String, String>? = HashMap()

) {
    val data = Base64.getEncoder().encodeToString(jacksonObjectMapper().writeValueAsBytes(dataObject))
    val attributes = PubSubAttributes(operation, additional)
}