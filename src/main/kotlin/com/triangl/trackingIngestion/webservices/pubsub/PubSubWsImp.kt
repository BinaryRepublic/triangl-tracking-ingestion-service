package com.triangl.trackingIngestion.webservices.pubsub

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutures
import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.PubsubMessage
import com.triangl.trackingIngestion.dto.PubSubAttributesDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*

@Service
@Profile("production")
class PubSubWsImp: PubSubWs {

    @Value("\${pubsub.topicId}")
    val topicId: String? = null

    override fun publish(data: Any, attributes: PubSubAttributesDto) {

        val dataByteString = jacksonObjectMapper().writeValueAsString(data)

        val topicName = ProjectTopicName.of(ServiceOptions.getDefaultProjectId(), topicId)
        var publisher: Publisher? = null
        val futures = ArrayList<ApiFuture<String>>()

        try {
            publisher = Publisher.newBuilder(topicName).build()

            val pubsubMessage = PubsubMessage.newBuilder()
                    .setData(ByteString.copyFromUtf8(dataByteString))
                    .putAllAttributes(attributes.toHashMap())
                    .build()

            val future = publisher!!.publish(pubsubMessage)
            futures.add(future)

        } finally {
            //ApiFutures.allAsList(futures).get()
            publisher?.shutdown()
        }
    }
}