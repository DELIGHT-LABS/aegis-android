package io.delightlabs.aegis.citadel

import io.delightlabs.aegis.Aegis
import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.decrypt
import io.delightlabs.aegis.encrypt
import io.delightlabs.aegis.protocol.Version as ProtocolVersion
import io.delightlabs.aegis.crypt.cipher.Version as CipherVersion
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CitadelUnitTest {

    @Test
    fun `citadel 1`() {
        val secret = "MESSAGE_1".toByteArray()
        val password = "01234567890123456789012345678901".toByteArray()

        val encryptedSecret = encrypt(CipherVersion.V1, secret, password)

        val aegis = Aegis.dealShares(
            ProtocolVersion.V1,
            Algorithm.NO_CRYPT,
            3,
            3,
            encryptedSecret,
        )

        val token = "eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ5b3VuZ0BkZWxpZ2h0bGFicy5pbyIsImV4cCI6MTc0Njc4NDczMCwianRpIjoiOGJmYTVkMjgtZjkxZi00YmQ3LTgxYTMtZGM4NjllYWVkNmYyIiwic3NvX3Byb3ZpZGVyIjoiR29vZ2xlIn0.ukJ0gYQsZRE8gktRtzxA6cfPH97zWzwLTmU8DODX9sOSwnLPJ0dFFssTbQm0WE-Cfl95COAAl6WwuQ6NSVEIDg"
        val urls = listOf(
            Url("http://34.124.155.209:8080"),
            Url("http://34.124.155.209:8081"),
            Url("http://34.124.155.209:8082")
        )
        val citadel = Citadel(token, urls)
        val uuid = "8bfa5d28-f91f-4bd7-81a3-dc869eaed6f2".toByteArray()
        runBlocking {
            citadel.store(aegis.payloads, uuid)

            val retrieved = citadel.retrieve(uuid)
            assert(retrieved.size == 3)

            val encryptedRes = Aegis.combineShares(retrieved)

            val decryptedRes = decrypt(encryptedRes, password)
            assert(decryptedRes.contentEquals(secret))
        }
    }
}