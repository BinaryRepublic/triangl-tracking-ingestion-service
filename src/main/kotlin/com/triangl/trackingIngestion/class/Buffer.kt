package com.triangl.trackingIngestion.`class`

import com.triangl.trackingIngestion.entity.DatapointGroup
import com.triangl.trackingIngestion.entity.InputDataPoint
import io.grpc.netty.shaded.io.netty.util.internal.ConcurrentSet
import java.util.concurrent.ConcurrentHashMap

class Buffer {
    val data = ConcurrentHashMap<String, ConcurrentSet<DatapointGroup>>()

    fun insert(inputDataPoint: InputDataPoint) {
        if (data.containsKey(inputDataPoint.deviceId)) {
            val datapointGroup = data[inputDataPoint.deviceId]!!.find {
                inputDataPoint.timestamp in it.startInstant..it.endInstant
            }

            if (datapointGroup != null) {
                datapointGroup.dataPoints.add(inputDataPoint)
            } else {
                val newDatapointGroup = DatapointGroup(
                    inputDataPoint
                )

                data[inputDataPoint.deviceId]!!.add(newDatapointGroup)
            }
        } else {
            val newDatapointGroup = DatapointGroup(
                inputDataPoint
            )

            val datapointGroups = ConcurrentSet<DatapointGroup>()
            datapointGroups.add(newDatapointGroup)
            data[inputDataPoint.deviceId] = datapointGroups
        }
    }

    fun read(): MutableMap<String, ConcurrentSet<DatapointGroup>> {
        return data
    }
}