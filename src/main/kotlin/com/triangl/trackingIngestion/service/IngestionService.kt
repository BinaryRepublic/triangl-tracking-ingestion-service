package com.triangl.trackingIngestion.service

import com.triangl.trackingIngestion.dto.PubSubAttributesAdditionalDto
import com.triangl.trackingIngestion.dto.PubSubAttributesDto
import com.triangl.trackingIngestion.dto.PubSubOperation
import com.triangl.trackingIngestion.entity.TrackingPoint
import com.triangl.trackingIngestion.webservices.datastore.DatastoreWs
import com.triangl.trackingIngestion.webservices.pubsub.PubSubWs
import org.springframework.stereotype.Service

@Service("ingestionService")
class IngestionService (
    private val datastoreWs: DatastoreWs,
    private val pubSubWs: PubSubWs
) {
    fun insertTrackingPoint(trackingPoint: TrackingPoint, mapId: String): TrackingPoint {
        val key = datastoreWs.saveTrackingPoint(trackingPoint)

        val dbTrackingPoint = datastoreWs.getTrackingPointByKey(key)

        pubSubWs.publish(
            listOf(dbTrackingPoint),
            PubSubAttributesDto().apply {
                operation = PubSubOperation.APPLY_TRACKING_POINT
                additional = PubSubAttributesAdditionalDto(mapId)
            }
        )

        return dbTrackingPoint
    }
}