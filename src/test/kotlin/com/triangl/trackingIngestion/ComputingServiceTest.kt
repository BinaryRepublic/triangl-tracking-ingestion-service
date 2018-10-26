package com.triangl.trackingIngestion

import com.nhaarman.mockito_kotlin.check
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.entity.Map
import com.triangl.trackingIngestion.service.ComputingService
import com.triangl.trackingIngestion.service.IngestionService
import com.triangl.trackingIngestion.webservices.datastore.DatastoreWs
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ComputingServiceWrapper(
    ingestionService: IngestionService,
    datastoreWs: DatastoreWs
): ComputingService(ingestionService, datastoreWs) {
    fun computeFromRSSIPublic(datapointGroup: DatapointGroup) = computeFromRSSI(datapointGroup)
    fun parseCustomerRoutersIntoHashmapPublic(customer: Customer): HashMap<String, Router> = parseCustomerRoutersIntoHashmap(customer)
    fun addRouterToRouterDataPointsPublic(routerDataPointList: List<RouterDataPoint>, routerHashMap: HashMap<String, Router>) = addRouterToRouterDataPoints(routerDataPointList, routerHashMap)
}

@RunWith(MockitoJUnitRunner::class)
class ComputingServiceTest {
    @Mock
    private lateinit var ingestionService: IngestionService

    @Mock
    private lateinit var datastoreWs: DatastoreWs

    @InjectMocks
    private lateinit var computingService: ComputingServiceWrapper

    private val nowString = "2018-09-25 13:49:09"
    private val now = LocalDateTime.parse(nowString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    private val inputDataPoint1 = InputDataPoint("RouterId1", "DeviceId1", "associatedAP",nowString, -255)
    private val inputDataPoint2 = InputDataPoint("RouterId2", "DeviceId1", "associatedAP",nowString, -200)
    private val inputDataPoint3 = InputDataPoint("RouterId3", "DeviceId1", "associatedAP",nowString, -180)

    private val router1 = Router("RouterId1", Coordinate(x = 1f, y = 2f))
    private val router2 = Router("RouterId2", Coordinate(x = 2f, y = 3f))
    private val router3 = Router("RouterId3", Coordinate(x = 3f, y = 4f))
    private val routerList = arrayListOf(router1, router2, router3)

    private val map = Map(id = "MapId1", name = "Map1", router = routerList)
    private val customer = Customer(name = "Customer1", maps = listOf(map))

    @Test
    fun `should insert element to Buffer`() {
        /* When */
        computingService.insertToBuffer(inputDataPoint1)

        /* Then */
        val bufferState = computingService.readFromBuffer()
        assertThat(bufferState["DeviceId1"]!!.size, `is`(1))
        assertThat(bufferState["DeviceId1"]!!.first().dataPoints.size, `is`(1))

        bufferState.clear()
    }

    @Test
    fun `should group elements where the timestamps are close together`() {
        /* When */
        computingService.insertToBuffer(inputDataPoint1)
        computingService.insertToBuffer(inputDataPoint2)
        computingService.insertToBuffer(inputDataPoint3)

        /* Then */
        val bufferState = computingService.readFromBuffer()
        assertThat(bufferState["DeviceId1"]!!.size, `is`(1))
        assertThat(bufferState["DeviceId1"]!!.first().dataPoints.size, `is`(3))

        bufferState.clear()
    }

    @Test
    fun `should compute and insert new TrackingPoint`() {
        /* Given */
        val datapointGroup = DatapointGroup(now, "Device1")
        datapointGroup.dataPoints.addAll(listOf(inputDataPoint1 ,inputDataPoint2 ,inputDataPoint3))
        val highestRSSI = datapointGroup.dataPoints.minBy { it -> it.signalStrength }
        val correctLocation = routerList.first { it -> it.id == highestRSSI!!.routerId }

        given(datastoreWs.getCustomerByRouterId(routerList.map { it.id!! }[0])).willReturn(customer)

        /* When */
        computingService.computeFromRSSIPublic(datapointGroup)

        /* Then */
        verify(ingestionService).insertTrackingPoint(check {
            assertThat(it.deviceId, `is`(datapointGroup.deviceId))
            assertThat(it.routerDataPoints!!.size, `is`(3))
            assertThat(it.location!!.x, `is`(correctLocation.location!!.x))
            assertThat(it.location!!.y, `is`(correctLocation.location!!.y))
        }, check {
            assertThat(it, `is`(map.id))
        })

    }

    @Test
    fun `should parse Routers into Hashmap`() {
        /* When */
        val hashMapResult = computingService.parseCustomerRoutersIntoHashmapPublic(customer)

        /* Then */
        assertThat(hashMapResult[router1.id], `is`(router1))
        assertThat(hashMapResult[router2.id], `is`(router2))
        assertThat(hashMapResult[router3.id], `is`(router3))
    }

    @Test
    fun `should add Router from HashMap to RouterDataPoints`() {
        /* Given */
        val incompleteRouter1 = Router(router1.id)

        val routerDataPoint1 = RouterDataPoint(router = incompleteRouter1, timestamp = now.toString())
        val routerDataPointList = listOf(routerDataPoint1)

        val routerHashMap = hashMapOf(router1.id!! to router1)

        /* When */
        computingService.addRouterToRouterDataPointsPublic(routerDataPointList, routerHashMap)

        /* Then */
        assertThat(routerDataPointList[0].router, `is`(router1))
    }
}