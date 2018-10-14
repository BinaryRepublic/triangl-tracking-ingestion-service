package com.triangl.trackingIngestion.dto

import com.fasterxml.jackson.databind.ObjectMapper

class PubSubAttributesDto {

    lateinit var operation: PubSubOperation

    lateinit var additional: PubSubAttributesAdditionalDto

    fun toHashMap(): HashMap<String, String> {
        val objectMapper = ObjectMapper()

        return hashMapOf(
            "operation" to operation.toString(),
            "additional" to objectMapper.writeValueAsString(additional)
        )
    }
}

enum class PubSubOperation {
    APPLY_TRACKING_POINT
}