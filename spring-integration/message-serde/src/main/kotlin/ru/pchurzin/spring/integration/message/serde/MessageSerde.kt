package ru.pchurzin.spring.integration.message.serde

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.serializer.Deserializer
import org.springframework.core.serializer.Serializer
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID



fun main() {
    val objectMapper = jacksonObjectMapper()
    val serializer = JacksonMessageSerializer(objectMapper)
    val deserializer = JacksonMessageDeserializer(objectMapper)

    val originalMessage = MessageBuilder.withPayload(SomeMessage("foo", 42)).build()
    val json = serializer.serializeToByteArray(originalMessage).toString(Charsets.UTF_8)
    val deserializedMessage = deserializer.deserializeFromByteArray(json.toByteArray())

    check(deserializedMessage == originalMessage)
}

data class SomeMessage(val a: String, val b: Int)

class JacksonMessageSerializer(private val objectMapper: ObjectMapper) : Serializer<Message<*>> {
    override fun serialize(message: Message<*>, outputStream: OutputStream) {
        objectMapper.writeValue(outputStream, getRootArray(message))
    }

    private fun getRootArray(message: Message<*>) =
        mapOf(
            "type" to message.payload::class.qualifiedName,
            "headers" to message.headers.toMap(),
            "payload" to message.payload
        )

    override fun serializeToByteArray(message: Message<*>): ByteArray {
        return objectMapper.writeValueAsBytes(getRootArray(message))
    }
}

class JacksonMessageDeserializer(private val objectMapper: ObjectMapper) : Deserializer<Message<*>> {

    override fun deserialize(inputStream: InputStream): Message<*> = deserialize(objectMapper.readTree(inputStream))

    private fun deserialize(rootNode: JsonNode): Message<*> {
        val headers = HelperMessageHeaders(objectMapper.convertValue<Map<String, Any>>(rootNode["headers"]))
        val payloadClass = objectMapper.convertValue<Class<*>>(rootNode["type"])
        val payload = objectMapper.convertValue(rootNode["payload"], payloadClass)
        return MessageBuilder.createMessage(payload, headers)
    }

    override fun deserializeFromByteArray(serialized: ByteArray): Message<*> =
        deserialize(objectMapper.readTree(serialized))
}

private class HelperMessageHeaders(
    headers: Map<String, Any>
) : MessageHeaders(headers, UUID.fromString(headers[ID].toString()), headers[TIMESTAMP] as Long?)