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
data class SecretPayload(val overwrite: Boolean, val secret: String)

@Serializable
data class SecretResponse(val secret: String)

class Citadel(private val token: String, private val urls: List<Url>)  {
    private val forts: List<Fort> = urls.map { Fort(token, it) }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun store(payloads: List<String>, key: ByteArray) {
        if (payloads.size != forts.size) {
            throw Exception("Payloads and Fort do not match")
        }

        val responses = mutableListOf<Deferred<Unit>>()
        for (i in payloads.indices) {
            val res = coroutineScope {
                async { putSecret(forts[i], payloads[i], true) }
            }
            responses.add(res)
        }

        responses.awaitAll()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun retrieve(key: ByteArray): List<String> {
        val strKey = Base64.getEncoder().encodeToString(key)

        val responses = mutableListOf<Deferred<String>>()

            coroutineScope {
                supervisorScope {
                    forts.map { fort ->
                        val res = async { getSecret(fort) }
                        responses.add(res)
                    }
                }
            }

        val res = mutableListOf<String>()
        responses.forEach { response ->
            try {
                val r = response.await()
                val secretResponse = Gson().fromJson(r, SecretResponse::class.java)
                res.add(secretResponse.secret)
            } catch (err: Exception) {
                println("Error: $err")
            }
        }

        return res
    }

    @OptIn(InternalAPI::class)
    private suspend fun putSecret(fort: Fort, secret: String, overwrite: Boolean = true) {
        val payload = SecretPayload(overwrite, secret)
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }

        val response: HttpResponse = client.put("${fort.url}/api/v0/secret") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${fort.token}")
            setBody(payload)
        }

        if (!(response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created)) {
            throw Exception("HTTP error! status: ${response.status}")
        }

        client.close()
    }

    private suspend fun getSecret(fort: Fort): String {
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