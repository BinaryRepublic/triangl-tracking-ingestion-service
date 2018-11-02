package com.triangl.trackingIngestion.webservices.datastore

import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.StructuredQuery.OrderBy
import com.google.common.collect.Lists
import com.googlecode.objectify.Key
import com.googlecode.objectify.ObjectifyService.ofy
import com.triangl.trackingIngestion.dto.RouterLastSeenDto
import com.triangl.trackingIngestion.entity.Customer
import com.triangl.trackingIngestion.entity.TrackingPoint
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service


@Service
@Profile("production")
class DatastoreWsImp: DatastoreWs {
    override fun getRouterLastSeenList(): List<RouterLastSeenDto> {
        val datastore = DatastoreOptions.getDefaultInstance().service

        val query = Query.newProjectionEntityQueryBuilder()
            .setKind("TrackingPoint")
            .setOrderBy(
                OrderBy.asc("routerDataPoints.router.id"),
                OrderBy.desc("createdAt")
            )
            .setProjection("routerDataPoints.router.id", "createdAt")
            .setDistinctOn("routerDataPoints.router.id")
            .build()

        val result = datastore.run(query)

        return Lists.newArrayList(result).map {
            RouterLastSeenDto(
                routerId = it.getString("routerDataPoints.router.id"),
                lastSeen = it.getString("createdAt")
            )
        }
    }

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