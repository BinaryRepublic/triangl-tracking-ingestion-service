package com.triangl.trackingIngestion.webservices.pubsub

import com.triangl.trackingIngestion.entity.TrackingPoint

interface PubSubWs {
    fun sendCustomerToPubSub(trackingPoint: TrackingPoint)
}