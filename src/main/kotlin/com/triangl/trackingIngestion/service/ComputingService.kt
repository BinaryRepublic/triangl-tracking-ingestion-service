package com.triangl.trackingIngestion.service

import com.triangl.trackingIngestion.entity.DatapointGroup
import com.triangl.trackingIngestion.entity.InputDataPoint
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.springframework.stereotype.Service

var buffer = HashMap<String, ArrayList<DatapointGroup>>().withDefault { arrayListOf() }

@Service("computingService")
class ComputingService {
    fun insertToBuffer(inputDataPoint: InputDataPoint) {
        if (buffer.containsKey(inputDataPoint.deviceId)) {

            val datapointGroup = buffer[inputDataPoint.deviceId]!!.find { it.startInstant <= inputDataPoint.timestamp!! && inputDataPoint.timestamp!! <= it.endInstant }

            if (datapointGroup != null) {
                datapointGroup.dataPoints!!.add(inputDataPoint)
            } else {
                buffer[inputDataPoint.deviceId]!!.add(DatapointGroup(inputDataPoint.timestamp!!))
            }

        } else {
            val newDatapointGroup = DatapointGroup(inputDataPoint.timestamp!!).apply { dataPoints = arrayListOf(inputDataPoint) }
            buffer[inputDataPoint.deviceId!!] = arrayListOf(newDatapointGroup)
        }
    }
    fun readFromBuffer():MutableMap<String, ArrayList<DatapointGroup>> = buffer

    fun startBufferWatcher() {
        launch {
            while (true) {
                deleteTimeouts()
                findElementsToCompute()
                compute()
                insertToDB()
                delay(500)
            }
        }
    }

    private fun deleteTimeouts() {

    }

    private fun findElementsToCompute() {

    }

    private fun compute () {

    }

    private fun insertToDB () {

    }
}