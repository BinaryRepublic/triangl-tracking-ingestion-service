package com.triangl.trackingIngestion

import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.entity.Map
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.ZoneOffset

@RunWith(MockitoJUnitRunner::class)
class TrackingPointTest {

    private val timeStampString = "2018-09-25 13:49:09"

    private val inputDataPoint1 = InputDataPoint("RouterId1", "DeviceId1", "associatedAP",timeStampString, -255)
    private val inputDataPoint2 = InputDataPoint("RouterId2", "DeviceId1", "associatedAP",timeStampString, -200)
    private val inputDataPoint3 = InputDataPoint("RouterId3", "DeviceId1", "associatedAP",timeStampString, -180)

    private val router1 = Router("RouterId1", Coordinate(x = 1f, y = 2f))
    private val router2 = Router("RouterId2", Coordinate(x = 2f, y = 3f))
    private val router3 = Router("RouterId3", Coordinate(x = 3f, y = 4f))
    private val routerList = arrayListOf(router1, router2, router3)

    private val map = Map(id = "MapId1", name = "Map1", router = routerList)
    private val customer = Customer(name = "Customer1", maps = listOf(map))

    @Test
    fun `should add Router from HashMap to RouterDataPoints`() {
        /* Given */
        val routerDataPointList = listOf(inputDataPoint1,inputDataPoint2,inputDataPoint3).map {
            RouterDataPoint(
                router = Router(it.routerId),
                associatedAP = it.associatedAP,
                signalStrength = it.signalStrength,
                timestamp = it.timestamp.toInstant(ZoneOffset.UTC).toString()
            )
        }

        val newTrackingPoint = TrackingPoint(
            deviceId = inputDataPoint1.deviceId,
            routerDataPoints = routerDataPointList
        )

        val routerWithCoordinatesHashMap = customer.toRoutersHashmap()

        /* When */
        newTrackingPoint.fillMissingRouterCoordinates(routerWithCoordinatesHashMap)

        /* Then */
        MatcherAssert.assertThat(newTrackingPoint.routerDataPoints[0].router, Matchers.`is`(router1))
        MatcherAssert.assertThat(newTrackingPoint.routerDataPoints[1].router, Matchers.`is`(router2))
        MatcherAssert.assertThat(newTrackingPoint.routerDataPoints[2].router, Matchers.`is`(router3))
    }
}