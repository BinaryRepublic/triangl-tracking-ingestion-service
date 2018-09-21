package com.triangl.trackingIngestion.service

import com.triangl.trackingIngestion.entity.TrackingPoint
import com.triangl.trackingIngestion.webservices.datastore.DatastoreWs
import org.springframework.stereotype.Service

@Service("ingestionService")
class IngestionService (
    private val datastoreWs: DatastoreWs
) {
    fun insertTrackingPoint(trackingPoint: TrackingPoint): TrackingPoint {
        datastoreWs.saveTrackingPoint(trackingPoint)
        return trackingPoint
    }
}