package io.delightlabs.aegis.citadel

import io.delightlabs.aegis.Aegis
import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.decrypt
import io.delightlabs.aegis.encrypt
import io.delightlabs.aegis.protocol.gson
import io.delightlabs.aegis.protocol.Version as ProtocolVersion
import io.delightlabs.aegis.crypt.cipher.Version as CipherVersion
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import org.junit.Test

data class TestAegisSecret (
    val wallet: List<TestWallet>
)

data class TestWallet (
    val address: String,
    val name: String,
    val publicKey: String,
    val encrypted: String
)

class CitadelUnitTest {

    @Test
    fun `citadel 1`() {
        val password = "01234567890123456789012345678901".toByteArray()
        val data = "MESSAGE_1".toByteArray()
        val salt = "SALT_1".toByteArray()

        val encryptedData = encrypt(CipherVersion.V1, data, password, salt)

        val aegisSecretData = TestAegisSecret(
            listOf(
                TestWallet(
                    "xpla1xxxx",
                    "name1",
                    "publicKey1",
                    encryptedData
                )
            )
        )

        val secret = gson.toJson(aegisSecretData).toByteArray()

        val aegis = Aegis.dealShares(
            ProtocolVersion.V1,
            Algorithm.NO_CRYPT,
            3,
            3,
            secret,
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

            val resAegisSecret = gson.fromJson(String(encryptedRes), TestAegisSecret::class.java)

            val decryptedRes = decrypt(resAegisSecret.wallet[0].encrypted, password, salt)
            assert(decryptedRes.contentEquals(data))
        }
    }
}