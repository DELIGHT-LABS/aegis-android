package io.delightlabs.aegis.citadel

import io.delightlabs.aegis.Aegis
import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.protocol.Version
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CitadelUnitTest {

    @Test
    fun `citadel 1`() {
        val secret = "MESSAGE_1".toByteArray()
        val key = "01234567890123456789012345678901".toByteArray()

        val aegis = Aegis.dealShares(
            Version.V1,
            io.delightlabs.aegis.crypt.cipher.Version.V1,
            Algorithm.NO_CRYPT,
            3,
            3,
            secret,
            key
        )

        val token = "eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI1ODhiOTZmYy03ZGFkLTRmNmQtYjczNy1iZDE2YjNmMGZmNTgiLCJzc29fcHJvdmlkZXIiOiJHb29nbGUiLCJpYXQiOjE3MTY1ODc0NjZ9.BvBltHARZwW3gDKFEw5kO1gG_89ikLUJu4Xfx5zpRY2o9j405idWq4kygiC7Iqnga4Zs7VBJF7aBRg_d6gk3Bg"
        val urls = listOf(
            Url("http://34.124.155.209:8080"),
            Url("http://34.124.155.209:8081"),
            Url("http://34.124.155.209:8082")
        )
        val citadel = Citadel(token, urls)
        val uuid = "588b96fc-7dad-4f6d-b737-bd16b3f0ff58".toByteArray()
        runBlocking {
            citadel.store(aegis.payloads, uuid)

            val retrieved = citadel.retrieve(uuid)
            assert(retrieved.size == 3)

            val aegis2 = Aegis()
            aegis2.payloads = retrieved
            val msg = aegis2.combineShares(key)
            assert(msg.contentEquals(secret))
        }
    }
}