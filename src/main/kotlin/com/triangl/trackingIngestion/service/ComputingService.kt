package com.triangl.trackingIngestion.service

import com.googlecode.objectify.ObjectifyService
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver
import com.lemmingapex.trilateration.TrilaterationFunction
import com.triangl.trackingIngestion.`class`.Buffer
import com.triangl.trackingIngestion.dto.RouterLastSeenDto
import com.triangl.trackingIngestion.entity.*
import com.triangl.trackingIngestion.webservices.datastore.DatastoreWs
import io.grpc.netty.shaded.io.netty.util.internal.ConcurrentSet
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset


var buffer = Buffer()
@Service("computingService")
class ComputingService (
    var ingestionService: IngestionService,
    var datastoreWs: DatastoreWs
) {
    @Value("\${mac.salt}")
    lateinit var salt: String

    @Value("\${mac.pepper}")
    lateinit var pepper: String

    fun insertToBuffer(inputDataPoint: InputDataPoint) {
        val now = LocalDateTime.now()

        val notBeingHashed = inputDataPoint.deviceId.substring(0,8)     // this is the manufacturer identifier
        val beingHashed = inputDataPoint.deviceId.substring(8)

        val hash = notBeingHashed + BCrypt.hashpw(beingHashed + pepper, salt)

        println("$hash  ${inputDataPoint.signalStrength}    ${inputDataPoint.routerId}      ${inputDataPoint.timestamp}     $now")

        buffer.insert(inputDataPoint)
    }

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

            trackingPoints.forEach { (newTrackingPoint, _) ->
                newTrackingPoint.hashMacAddress(salt, pepper)
            }

            ObjectifyService.run {
                trackingPoints.forEach { (newTrackingPoint, mapId) ->
                    ingestionService.insertTrackingPoint(newTrackingPoint, mapId)
                }
            }
        }
    }

    fun findElementsToCompute(datapointGroups: ConcurrentSet<DatapointGroup>, deviceId: String):ArrayList<DatapointGroup> {
        val now = LocalDateTime.now()
        val valuesToCompute = arrayListOf<DatapointGroup>()
        val valuesToRemove = arrayListOf<DatapointGroup>()

        datapointGroups.forEach {datapointGroup ->
            val numberOfUniqueRoutersInDataPoints = datapointGroup.dataPoints.map { it.routerId }.toSet().size
            if (
                (datapointGroup.timeoutInstant < now && numberOfUniqueRoutersInDataPoints == 2) ||
                numberOfUniqueRoutersInDataPoints >= 3
            ) {
                valuesToCompute.add(datapointGroup)
            } else if (datapointGroup.timeoutInstant < now) {
                valuesToRemove.add(datapointGroup)
            }
        }

        datapointGroups.removeAll(valuesToCompute)
        datapointGroups.removeAll(valuesToRemove)

        if (datapointGroups.size == 0) {
            buffer.data.remove(deviceId)
        }
        return valuesToCompute
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

        val routersToLookUp = datapointGroup.dataPoints.map { it.routerId }

        val newTrackingPoint = TrackingPoint(
            deviceId = datapointGroup.deviceId,
            routerDataPoints = routerDataPointList
        )

        val customerObj = datastoreWs.getCustomerByRouterId(routersToLookUp[0])
        val routerWithCoordinatesHashMap = customerObj!!.toRoutersHashmap()

        newTrackingPoint.fillMissingRouterCoordinates(routerWithCoordinatesHashMap)

        val strongestRSSI = newTrackingPoint.routerDataPoints.maxBy { it.signalStrength!! }!!
        val location = computeLocationFromLaterating(newTrackingPoint)

        newTrackingPoint.setLocationAndTimestampFromRouterDataPoint(strongestRSSI)

        return Pair(
            newTrackingPoint,
            customerObj.maps!![0].id!!
        )
    }

    fun computeLocationFromLaterating(newTrackingPoint: TrackingPoint) {
        val positions = newTrackingPoint.routerDataPoints.map {
            doubleArrayOf(it.router!!.location!!.x!!.toDouble(), it.router!!.location!!.y!!.toDouble())
        }.toTypedArray()

        val distances = newTrackingPoint.routerDataPoints.map {
            convertRSSItoCentimeter(it.signalStrength!!)
        }.toDoubleArray()

        // Calculation from  https://github.com/lemmingapex/trilateration
        val solver = NonLinearLeastSquaresSolver(TrilaterationFunction(positions, distances), LevenbergMarquardtOptimizer())
        val optimum = solver.solve()

        // the answer
        val centroid = optimum.point.toArray()

        // error and geometry information; may throw SingularMatrixException depending the threshold argument provided
        // val standardDeviation = optimum.getSigma(0.0)
        // val covarianceMatrix = optimum.getCovariances(0.0)

    }

    fun convertRSSItoCentimeter(rssi: Int): Double {
        // https://electronics.stackexchange.com/a/83356
        val a = (-15).toDouble()  // is the received signal strength in dBm at 1 metre
        val n = 5.toDouble()      // is the propagation constant or path-loss exponent
        return (1 / Math.pow(10.toDouble(), ((rssi - a)/ (10*n)))) * 100
    }
}