package com.triangl.trackingIngestion.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.triangl.trackingIngestion.TrackingIngestionApplication.PubsubOutboundGateway
import com.triangl.trackingIngestion.entity.PubSubEvent
import com.triangl.trackingIngestion.entity.PubSubMessage
import com.triangl.trackingIngestion.entity.TrackingPoint
import com.triangl.trackingIngestion.webservices.datastore.DatastoreWs
import org.springframework.stereotype.Service

@Service("ingestionService")
class IngestionService (
    private val datastoreWs: DatastoreWs,
    private val messagingGateway: PubsubOutboundGateway
) {
    fun insertTrackingPoint(trackingPoint: TrackingPoint): TrackingPoint {
        val key = datastoreWs.saveTrackingPoint(trackingPoint)

        val pubSubEvent = PubSubEvent(trackingPoint, "APPLY_TRACKING_POINT", hashMapOf("mapId" to "TestMap"))
        val pubSubMessage = PubSubMessage(listOf(pubSubEvent))

        val jsonString = jacksonObjectMapper().writeValueAsString(pubSubMessage)

        messagingGateway.sendToPubsub(jsonString)

        return datastoreWs.getTrackingPointByKey(key)
    }
}