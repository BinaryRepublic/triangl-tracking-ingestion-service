package com.triangl.trackingIngestion

import com.nhaarman.mockito_kotlin.given
import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.entity.Map
import com.triangl.trackingIngestion.service.ComputingService
import com.triangl.trackingIngestion.service.IngestionService
import com.triangl.trackingIngestion.webservices.datastore.DatastoreWs
import io.grpc.netty.shaded.io.netty.util.internal.ConcurrentSet
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.ZoneOffset

@RunWith(MockitoJUnitRunner::class)
class ComputingServiceTest {
    @Mock
    private lateinit var ingestionService: IngestionService

    @Mock
    private lateinit var datastoreWs: DatastoreWs

    @InjectMocks
    private lateinit var computingService: ComputingService

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
    fun `should find elements to compute`() {
        /* Given */
        val datapointGroup = DatapointGroup(inputDataPoint1)
        datapointGroup.dataPoints.addAll(listOf(inputDataPoint2 ,inputDataPoint3))

        val datapointGroupList = ConcurrentSet<DatapointGroup>()
        datapointGroupList.add(datapointGroup)

        /* When */
        val elementsToCompute = computingService.findElementsToCompute(datapointGroupList, "DeviceId1")

        /* Then */
        assertThat(elementsToCompute.size, `is`(1))
        assertThat(elementsToCompute[0].dataPoints.size, `is`(3))
        assertThat(elementsToCompute[0].deviceId, `is`(inputDataPoint1.deviceId))
    }

    @Test
    fun `should compute new TrackingPoint`() {
        /* Given */
        val datapointGroup = DatapointGroup(inputDataPoint1)
        datapointGroup.dataPoints.addAll(listOf(inputDataPoint2 ,inputDataPoint3))
        val highestRSSI = datapointGroup.dataPoints.maxBy { it -> it.signalStrength }
        val correctLocation = routerList.first { it -> it.id == highestRSSI!!.routerId }

        given(datastoreWs.getCustomerByRouterId(routerList.map { it.id!! }[0])).willReturn(customer)

        /* When */
        val computedTrackingPoint = computingService.computeFromRSSI(datapointGroup)

        /* Then */
        with(computedTrackingPoint!!.first){
            assertThat(this.deviceId, `is`(datapointGroup.deviceId))
            assertThat(this.routerDataPoints.size, `is`(3))
            assertThat(this.location!!.x, `is`(correctLocation.location!!.x))
            assertThat(this.location!!.y, `is`(correctLocation.location!!.y))
            assertThat(this.timestamp!!, `is`(highestRSSI!!.timestamp.toInstant(ZoneOffset.UTC).toString()))
        }
        assertThat(computedTrackingPoint.second, `is`(map.id))
    }
}