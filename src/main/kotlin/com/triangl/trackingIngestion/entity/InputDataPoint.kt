package com.triangl.trackingIngestion.entity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import io.swagger.annotations.ApiModelProperty

class InputDataPoint (
    @ApiModelProperty(notes = "Id/MAC of the router who tracked the device")
    var routerId: String,

    @ApiModelProperty(notes = "Id/MAC of the tracked Device")
    var deviceId: String,

    timestampString: String,

    @ApiModelProperty(notes = "RSSI of the connection to the tracked device")
    var signalStrength: Int,

    @ApiModelProperty(notes = "Time of flight of the connection (currently not used)")
    var timeOfFlight: Long? = null
) {
    @ApiModelProperty(notes = "Timestamp and TimestampString in GMT+00:00 timezone")
    var timestamp: LocalDateTime = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}