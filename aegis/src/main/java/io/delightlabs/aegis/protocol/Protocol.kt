package io.delightlabs.aegis.protocol

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import io.delightlabs.aegis.common.Packet
import java.lang.Exception
import java.lang.reflect.Type
import java.util.Base64

enum class Version {
    UNSPECIFIED, V0, V1
}

interface Protocol {
    fun getVersion(): Version
    fun pack(v: Any): Packet
    fun unpack(packet: Packet): Any
}

data class Payload(
    val timestamp: Long,
    @SerializedName("protocol_version")
    val protocolVersion: Version,
    var packet: Packet
)

@RequiresApi(Build.VERSION_CODES.O)
fun pack(version: Version, v: Any, timestamp: Long): String {
    val p = Payload(timestamp, version, ByteArray(0))

    val pc = getProtocol(p.protocolVersion) ?: throw Exception("Unsupported protocol")

    p.packet = pc.pack(v)

    val data = gson.toJson(p).toByteArray()
    return Base64.getEncoder().encodeToString(data)
}

@RequiresApi(Build.VERSION_CODES.O)
fun unpack(data: String): Pair<Any, Long> {
    val decoded = Base64.getDecoder().decode(data)

    val p: Payload
    try {
        p = gson.fromJson(String(decoded), Payload::class.java)
    } catch (e: JsonSyntaxException) {
        throw Exception("Invalid data format")
    }

    val pc = getProtocol(p.protocolVersion) ?: throw Exception("Unsupported protocol")

    return Pair(pc.unpack(p.packet), p.timestamp)
}

fun getProtocol(version: Version): Protocol? {
    return when (version) {
        Version.V0 -> VersionV0()
        Version.V1 -> VersionV1()
        else -> null
    }
}

val gson = Gson().newBuilder()
    .registerTypeAdapter(ByteArray::class.java, ByteArrayTypeSerializer())
    .registerTypeAdapter(ByteArray::class.java, ByteArrayTypeDeerializer()).disableHtmlEscaping().create()

class ByteArrayTypeSerializer : JsonSerializer<ByteArray> {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun serialize(src: ByteArray?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(Base64.getEncoder().encodeToString(src))
    }
}

class ByteArrayTypeDeerializer : JsonDeserializer<ByteArray> {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ByteArray {
        return Base64.getDecoder().decode(json?.asString ?: "")
    }
}