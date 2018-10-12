package com.triangl.trackingIngestion.pubSubEntity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.triangl.trackingIngestion.entity.TrackingPoint
import java.util.*

class PubSubMessageDto (
        dataObject: TrackingPoint,
        operation: OperationType,
        additional: HashMap<String, String>? = HashMap()

) {
    val dataByteArray: ByteArray = jacksonObjectMapper().writeValueAsBytes(dataObject)
    val data = Base64.getEncoder().encodeToString(dataByteArray)
    val attributes = PubSubMessageAttributeDto(operation, additional)
}