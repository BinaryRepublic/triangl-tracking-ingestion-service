package com.triangl.trackingIngestion.controller

import com.triangl.trackingIngestion.dto.RouterLastSeenDto
import com.triangl.trackingIngestion.entity.DatapointGroup
import com.triangl.trackingIngestion.entity.InputDataPoint
import com.triangl.trackingIngestion.service.ComputingService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TrackingIngestionController (
    private val computingService: ComputingService
) {
    init {
        computingService.startBufferWatcher()
    }

    @ApiOperation(value = "Inserts one InputDataPoint into the buffer for computing")
    @ApiResponses(value = [(ApiResponse(code = 400, message = "Router ID not found"))])
    @PostMapping("/tracking")
    fun insertToBuffer(@RequestBody inputDataPoint: InputDataPoint): ResponseEntity<*> {
        return if (computingService.isRouterValid(inputDataPoint.routerId)) {
            computingService.insertToBuffer(inputDataPoint)
            ResponseEntity.noContent().build<Any>()
        } else {
            ResponseEntity.status(400).body(hashMapOf("error" to "Router ID not found"))
        }
    }

    @ApiOperation(value = "Inserts a list of InputDataPoints into the buffer for computing")
    @ApiResponses(value = [(ApiResponse(code = 400, message = "Router ID not found"))])
    @PostMapping("/tracking/multiple")
    fun insertManyToBuffer(@RequestBody inputDataPoints: List<InputDataPoint>): ResponseEntity<*> {
        val routerIds = inputDataPoints.map{it.routerId}.distinct()
        val invalidIds = routerIds.filter { !computingService.isRouterValid(it) }
        return if (invalidIds.isNotEmpty()) {
            ResponseEntity.status(400).body(hashMapOf("error" to "the collection contains invalid routerIds: $invalidIds"))
        } else {
            inputDataPoints.forEach {inputDataPoint ->
                computingService.insertToBuffer(inputDataPoint)
            }
            ResponseEntity.noContent().build<Any>()
        }
    }

    @ApiOperation(value = "Reads the content of the buffer for debugging", response = DatapointGroup::class, responseContainer = "Map")
    @GetMapping("/read")
    fun testRead(): ResponseEntity<*>{
        val buffer = computingService.readFromBuffer()

        return ResponseEntity.ok().body(hashMapOf("buffer" to buffer))
    }

    @ApiOperation(value = "Gets the timestamp for each Router when it was last sending packages", response = RouterLastSeenDto::class, responseContainer = "List")
    @GetMapping("/routers/lastSeen")
    fun getRoutersLastSeen(): ResponseEntity<*> {
        val routersLastSeen = computingService.getRoutersLastSeen()

        return ResponseEntity.ok().body(routersLastSeen)
    }
}