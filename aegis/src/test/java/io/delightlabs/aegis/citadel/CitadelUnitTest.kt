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

        val token = "eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ4cGxhLWdhbWVzIiwic3ViIjoidGVzdEBkZWxpZ2h0bGFicy5pbyIsImV4cCI6MTc4NzMxMzM0OCwianRpIjoiYWFhYWFhYWEtYmJiYi1jY2NjLWRkZGQtZWVlZWVlZWVlZWVlIiwic3NvX3Byb3ZpZGVyIjoiR29vZ2xlIn0.CXMj447bNXTQwKgkNrwYzucPYH5uxYGQmuDbfb1F2eIZMvhenXa3zYn0PlI4N16BbuG9Riv9Q_LoN4-bUuPcBg"
        val urls = listOf(
            Url("https://citadel-fort1.develop.delightlabs.dev"),
            Url("https://citadel-fort2.develop.delightlabs.dev"),
            Url("https://citadel-fort3.develop.delightlabs.dev"),
        )
        val citadel = Citadel(token, urls)
        val uuid = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee".toByteArray()
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