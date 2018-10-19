package com.triangl.trackingIngestion.webservices.datastore

import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.triangl.trackingIngestion.entity.Customer
import com.triangl.trackingIngestion.entity.TrackingPoint
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("production")
class DatastoreWsImp: DatastoreWs {
    override fun saveTrackingPoint(trackingPoint: TrackingPoint): Key<TrackingPoint> {
        return ofy().save().entity(trackingPoint).now()
    }

    override fun getTrackingPointByKey(key: Key<TrackingPoint>): TrackingPoint {
        return ofy().load().key(key).now()
    }

    override fun getCustomerByRouterId(routerId: String): Customer? {
        return ofy().load().type(Customer::class.java).filter("maps.router.id =", routerId).first().now()
    }
}