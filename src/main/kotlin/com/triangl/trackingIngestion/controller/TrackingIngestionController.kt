package com.triangl.trackingIngestion.controller

import com.triangl.trackingIngestion.entity.InputDataPoint
import com.triangl.trackingIngestion.service.ComputingService
import com.triangl.trackingIngestion.service.IngestionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TrackingIngestionController (
    private val computingService: ComputingService,
    private val ingestionService: IngestionService
) {
    init {
        computingService.startBufferWatcher()
    }

    @PostMapping("/tracking")
    fun computeAndIngest(@RequestBody inputDataPoint: InputDataPoint): ResponseEntity<*> {

        computingService.insertToBuffer(inputDataPoint)

        return ResponseEntity.ok().body(hashMapOf("received" to true))
        //ingestionService.insertTrackingPoint(trackingPoint)
    }

    @GetMapping("/read")
    fun testRead(): ResponseEntity<*>{
        val buffer = computingService.readFromBuffer()

        return ResponseEntity.ok().body(hashMapOf("buffer" to buffer))
    }
}