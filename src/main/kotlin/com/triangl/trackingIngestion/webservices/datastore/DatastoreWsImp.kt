package com.triangl.trackingIngestion.webservices.datastore

import com.googlecode.objectify.ObjectifyService.ofy
import com.triangl.trackingIngestion.entity.Customer
import com.triangl.trackingIngestion.entity.TrackingPoint
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("development")
class DatastoreWsImp: DatastoreWs {
    override fun saveTrackingPoint(trackingPoint: TrackingPoint) {
        ofy().save().entity(trackingPoint)
    }

    override fun getRoutersById(IDList: List<String>): Customer {
        return ofy().load().type(Customer::class.java).filter("maps.router.id in", IDList).first().now()
    }
}