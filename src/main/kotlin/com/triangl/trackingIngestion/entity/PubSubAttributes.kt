package com.triangl.trackingIngestion.entity

class PubSubAttributes (
        val operation: String,
        val additional: HashMap<String, String>? = HashMap()
)