package com.triangl.trackingIngestion.webservices.pubsub

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.triangl.trackingIngestion.TrackingIngestionApplication
import com.triangl.trackingIngestion.entity.TrackingPoint
import com.triangl.trackingIngestion.pubSubEntity.OperationType
import com.triangl.trackingIngestion.pubSubEntity.PubSubDto
import com.triangl.trackingIngestion.pubSubEntity.PubSubMessageDto
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("production")
class PubSubWsImp (
        private val messagingGateway: TrackingIngestionApplication.PubsubOutboundGateway
): PubSubWs {
    override fun sendCustomerToPubSub(trackingPoint: TrackingPoint) {
        val pubSubEvent = PubSubMessageDto(trackingPoint, OperationType.APPLY_TRACKING_POINT)
        val pubSubMessage = PubSubDto(listOf(pubSubEvent))
        val jsonString = jacksonObjectMapper().writeValueAsString(pubSubMessage)
        messagingGateway.sendToPubsub(jsonString)
    }
}