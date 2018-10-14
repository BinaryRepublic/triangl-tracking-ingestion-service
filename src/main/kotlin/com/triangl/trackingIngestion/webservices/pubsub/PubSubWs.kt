package com.triangl.trackingIngestion.webservices.pubsub

import com.triangl.trackingIngestion.dto.PubSubAttributesDto

interface PubSubWs {

    fun publish(data: Any, attributes: PubSubAttributesDto)
}