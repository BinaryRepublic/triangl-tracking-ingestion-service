package com.triangl.trackingIngestion.webservices.pubsub

import com.triangl.trackingIngestion.entity.TrackingPoint
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class PubSubWsMock: PubSubWs {
    override fun sendCustomerToPubSub(trackingPoint: TrackingPoint) { }
}