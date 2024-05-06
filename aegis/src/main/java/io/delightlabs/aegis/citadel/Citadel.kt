package io.delightlabs.aegis.citadel
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import io.delightlabs.aegis.payload
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.gson
import io.ktor.util.InternalAPI

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.util.Base64

data class Fort(
    val token: String,
    var url: Url
)

@Serializable
data class SecretPayload(val overwrite: Boolean, val secret: String, val id: String)

@Serializable
data class SecretResponse(val secret: String)

class Citadel(private val token: String, private val urls: List<Url>)  {
    private val forts: List<Fort> = urls.map { Fort(token, it) }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun store(payloads: List<payload>, key: ByteArray) {
        if (payloads.size != forts.size) {
            throw Exception("Payloads and Fort do not match")
        }

        val strKey = Base64.getEncoder().encodeToString(key)

        val responses = mutableListOf<Deferred<Unit>>()
        for (i in payloads.indices) {
            // encode to base64
            val data = Base64.getEncoder().encodeToString(payloads[i])

            val res = coroutineScope {
                async { postSecret(forts[i], strKey, data, true) }
            }
            responses.add(res)
        }

        responses.awaitAll()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun retrieve(key: ByteArray): List<payload> {
        val strKey = Base64.getEncoder().encodeToString(key)

        val responses = mutableListOf<Deferred<String>>()

            coroutineScope {
                supervisorScope {
                    forts.map { fort ->
                        val res = async { getSecret(fort, strKey) }
                        responses.add(res)
                    }
                }
            }

        val res = mutableListOf<payload>()
        responses.forEach { response ->
            try {
                val r = response.await()
                val secretResponse = Gson().fromJson(r, SecretResponse::class.java)
                res.add(Base64.getDecoder().decode(secretResponse.secret))
            } catch (err: Exception) {
                println("Error: $err")
            }
        }

        return res
    }

    @OptIn(InternalAPI::class)
    private suspend fun postSecret(fort: Fort, id: String, secret: String, overwrite: Boolean = true) {
        val payload = SecretPayload(overwrite, secret, id)
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }

        val response: HttpResponse = client.post("${fort.url}/api/v0/secret") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${fort.token}")
            setBody(payload)
        }

        if (response.status != HttpStatusCode.OK) {
            throw Exception("HTTP error! status: ${response.status}")
        }

        client.close()
    }

    private suspend fun getSecret(fort: Fort, id: String): String {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response: HttpResponse = client.get("${fort.url}/api/v0/secret") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${fort.token}")
        }
        client.close()
        if (response.status != HttpStatusCode.OK) {
            throw Exception("HTTP error! status: ${response.status}")
        }


        return response.bodyAsText()
    }
}