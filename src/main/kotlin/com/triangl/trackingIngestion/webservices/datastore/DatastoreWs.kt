package com.triangl.trackingIngestion.webservices.datastore

import com.googlecode.objectify.Key
import com.triangl.trackingIngestion.entity.Customer
import com.triangl.trackingIngestion.entity.TrackingPoint

interface DatastoreWs {
    fun saveTrackingPoint(trackingPoint: TrackingPoint): Key<TrackingPoint>
    fun getTrackingPointByKey(key: Key<TrackingPoint>): TrackingPoint
    fun getRoutersById(IDList: List<String>): Customer
}