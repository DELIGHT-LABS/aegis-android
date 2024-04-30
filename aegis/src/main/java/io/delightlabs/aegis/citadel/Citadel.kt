package io.delightlabs.aegis.citadel
import io.delightlabs.aegis.protocol.Payload
import java.net.URL

data class Fort(
    val token: String,
    val url: URL
)

class Citadel(private val forts: List<Fort>) {

    fun store(payloads: List<Payload>, vararg keys: String): Nothing? {
        // TODO: implementation
        return null
    }

    fun retrieve(vararg keys: String): Pair<List<Payload>?, Nothing?> {
        // TODO: implementation
        return null to null
    }

    companion object {
        fun new(token: String, vararg urls: URL): Citadel {
            val forts = urls.map { Fort(token, it) }
            return Citadel(forts)
        }
    }
}