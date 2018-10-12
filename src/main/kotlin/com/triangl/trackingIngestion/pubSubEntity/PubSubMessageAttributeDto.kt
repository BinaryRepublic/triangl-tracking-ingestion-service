package com.triangl.trackingIngestion.pubSubEntity

class PubSubMessageAttributeDto (
        val operation: OperationType,
        val additional: HashMap<String, String>? = HashMap()
)

enum class OperationType {
    APPLY_TRACKING_POINT
}