package com.triangl.trackingIngestion.controller

import com.triangl.trackingIngestion.entity.TrackingPoint
import com.triangl.trackingIngestion.service.IngestionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TrackingIngestionController (
    private val ingestionService: IngestionService
) {
    @RequestMapping("/tracking")
    fun computeAndIngest(@RequestBody trackingPoint: TrackingPoint): ResponseEntity<*> {

        ingestionService.insertTrackingPoint(trackingPoint)

        return ResponseEntity.ok().body(hashMapOf("received" to true))
    }
}