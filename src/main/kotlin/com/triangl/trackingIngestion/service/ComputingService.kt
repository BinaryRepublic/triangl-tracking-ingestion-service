package com.triangl.trackingIngestion.service

import com.googlecode.objectify.ObjectifyService
import com.triangl.trackingIngestion.`class`.Buffer
import com.triangl.trackingIngestion.dto.RouterLastSeenDto
import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.webservices.datastore.DatastoreWs
import io.grpc.netty.shaded.io.netty.util.internal.ConcurrentSet
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

var buffer = Buffer()

@Service("computingService")
class ComputingService (
    var ingestionService: IngestionService,
    var datastoreWs: DatastoreWs
) {
    fun insertToBuffer(inputDataPoint: InputDataPoint) =
        buffer.insert(inputDataPoint)

    fun readFromBuffer(): MutableMap<String, ConcurrentSet<DatapointGroup>> =
        buffer.read()

    fun getRoutersLastSeen(): List<RouterLastSeenDto> =
        datastoreWs.getRouterLastSeenList()

    fun isRouterValid(routerId: String) =
        datastoreWs.getCustomerByRouterId(routerId) != null

    fun startBufferWatcher() {
        val sleepTime = 5000
        launch {
            while (true) {
                handleBuffer()
                delay(sleepTime)
            }
        }.invokeOnCompletion {
            startBufferWatcher()
        }
    }

    fun handleBuffer() {
        buffer.data.forEach { deviceId: String, datapointGroups: ConcurrentSet<DatapointGroup> ->
            val elementsToCompute = findElementsToCompute(datapointGroups, deviceId)

            var trackingPoints = ObjectifyService.run {
                elementsToCompute.map { elementToCompute ->
                    computeFromRSSI(elementToCompute)
                }
            }

            trackingPoints = trackingPoints.filterNotNull()

            trackingPoints.map {(newTrackingPoint, mapId) ->
                ingestionService.insertTrackingPoint(newTrackingPoint, mapId)
            }
        }
    }

    fun findElementsToCompute(datapointGroups: ConcurrentSet<DatapointGroup>, deviceId: String):ArrayList<DatapointGroup> {
        val now = LocalDateTime.now()
        val valuesToRemove = arrayListOf<DatapointGroup>()

        datapointGroups.forEach {datapointGroup ->
            if (datapointGroup.timeoutInstant < now /*&& datapointGroup.size >= 3*/) {       //for the computing based on RSSI is 1 inputDataPoint enough
                valuesToRemove.add(datapointGroup)
            } else if (datapointGroup.timeoutInstant < now) {
                valuesToRemove.add(datapointGroup)
            }
        }

        datapointGroups.removeAll(valuesToRemove)

        if (datapointGroups.size == 0) {
            buffer.data.remove(deviceId)
        }
        return valuesToRemove
    }

    fun computeFromRSSI (datapointGroup: DatapointGroup):Pair<TrackingPoint,String>? {
        if (datapointGroup.dataPoints.isEmpty()) {
            return null
        }

        val routerDataPointList = datapointGroup.dataPoints.map {
            RouterDataPoint(
                router = Router(it.routerId),
                associatedAP = it.associatedAP,
                signalStrength = it.signalStrength,
                timestamp = it.timestamp.toInstant(ZoneOffset.UTC).toString()
            )
        }

        val strongestRSSI = routerDataPointList.maxBy { it.signalStrength!! }!!
        val routersToLookUp = datapointGroup.dataPoints.map { it.routerId }

        val newTrackingPoint = TrackingPoint(
            deviceId = datapointGroup.deviceId,
            routerDataPoints = routerDataPointList
        )

        val customerObj = datastoreWs.getCustomerByRouterId(routersToLookUp[0])
        val routerWithCoordinatesHashMap = customerObj!!.toRoutersHashmap()

        strongestRSSI.router = routerWithCoordinatesHashMap[strongestRSSI.router!!.id]

        newTrackingPoint.fillMissingRouterCoordinates(routerWithCoordinatesHashMap)
        newTrackingPoint.setLocationAndTimestampFromRouterDataPoint(strongestRSSI)

        return Pair(
            newTrackingPoint,
            customerObj.maps!![0].id!!
        )
    }
}