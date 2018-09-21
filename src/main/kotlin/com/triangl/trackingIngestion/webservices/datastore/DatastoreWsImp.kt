package com.triangl.trackingIngestion.webservices.datastore

import com.googlecode.objectify.ObjectifyService.ofy
import com.triangl.trackingIngestion.entity.TrackingPoint
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("development")
class DatastoreWsImp: DatastoreWs {
    override fun saveTrackingPoint(trackingPoint: TrackingPoint) {
        ofy().save().entity(trackingPoint)
    }
}