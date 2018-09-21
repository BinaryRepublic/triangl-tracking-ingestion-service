package com.triangl.trackingIngestion.webservices.datastore

import com.triangl.trackingIngestion.entity.TrackingPoint

interface DatastoreWs {
    fun saveTrackingPoint(trackingPoint: TrackingPoint)
}