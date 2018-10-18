package com.triangl.trackingIngestion.entity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import io.swagger.annotations.ApiModelProperty

class InputDataPoint (
    @ApiModelProperty(notes = "Id/MAC of the router who tracked the device", required = true)
    var routerId: String,

    @ApiModelProperty(notes = "Id/MAC of the tracked Device", required = true)
    var deviceId: String,

    @ApiModelProperty(notes = "Id/MAC of the Access Point the tracked Device is connected to", required = false)
    var associatedAP: String? = null,

    timestampString: String,

    @ApiModelProperty(notes = "RSSI of the connection to the tracked device", required = true)
    var signalStrength: Int,

    @ApiModelProperty(notes = "Time of flight of the connection (currently not used)", required = false)
    var timeOfFlight: Long? = null
) {
    @ApiModelProperty(notes = "Timestamp and TimestampString in GMT+00:00 timezone computed from timestampString. DONT give this parameter in the requested but do it as 'timestampString'", required = true)
    var timestamp: LocalDateTime = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}