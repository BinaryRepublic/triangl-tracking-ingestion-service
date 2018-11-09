package com.triangl.trackingIngestion

import com.triangl.trackingIngestion.`class`.Buffer
import com.triangl.trackingIngestion.entity.InputDataPoint
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BufferTest {

    private val timeStampString = "2018-09-25 13:49:09"
    private val inputDataPoint1 = InputDataPoint("RouterId1", "DeviceId1", "associatedAP",timeStampString, -255)
    private val inputDataPoint2 = InputDataPoint("RouterId2", "DeviceId1", "associatedAP",timeStampString, -200)
    private val inputDataPoint3 = InputDataPoint("RouterId3", "DeviceId1", "associatedAP",timeStampString, -180)

    @Test
    fun `should insert element to Buffer`() {
        /* When */
        val buffer = Buffer()
        buffer.insert(inputDataPoint1)

        /* Then */
        val bufferState = buffer.read()

        MatcherAssert.assertThat(bufferState["DeviceId1"]!!.size, Matchers.`is`(1))
        MatcherAssert.assertThat(bufferState["DeviceId1"]!!.first().dataPoints.size, Matchers.`is`(1))
    }

    @Test
    fun `should group elements where the timestamps are close together`() {
        /* When */
        val buffer = Buffer()
        buffer.insert(inputDataPoint1)
        buffer.insert(inputDataPoint2)
        buffer.insert(inputDataPoint3)

        /* Then */
        val bufferState = buffer.read()

        MatcherAssert.assertThat(bufferState["DeviceId1"]!!.size, Matchers.`is`(1))
        MatcherAssert.assertThat(bufferState["DeviceId1"]!!.first().dataPoints.size, Matchers.`is`(3))
    }
}