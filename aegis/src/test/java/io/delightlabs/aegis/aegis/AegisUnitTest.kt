package io.delightlabs.aegis

import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.crypt.cipher.Version as CipherVerison
import io.delightlabs.aegis.protocol.Version as ProtocolVersion
import org.junit.Assert
import org.junit.Test
import java.util.Base64

class AegisUnitTest {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun `aegis dealshare & cobineshare`() {
        val secret = "MESSAGE_1".toByteArray()

        val aegis = Aegis.dealShares(ProtocolVersion.V1, Algorithm.NO_CRYPT, 3, 3, secret)

       val combined = Aegis.combineShares(aegis.payloads)
        Assert.assertEquals(secret.contentToString(), combined.contentToString())
    }

    @Test
    fun `encrypt & decrypt 1`() {
        val password = "PASSWORD_1".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val encrypted = encrypt(CipherVerison.V1, secret, password)
        Assert.assertEquals(
            "VjEAAAAAAAAAAAAAAAAAAElyU3VpWFo0TDhOQ0hJRFp6TGxmNERRcXczTFBhUkM1dHN6V1k3NUZBT0E9",
            Base64.getEncoder().encodeToString(encrypted)
        )

        val decrypted = decrypt(encrypted, password)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun `encrypt & decrypt 2`() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val encrypted = encrypt(CipherVerison.V1, secret, password)
        Assert.assertEquals(
            "VjEAAAAAAAAAAAAAAAAAAG9UTTRyeE9oREJsNlZEellnbFIrWGVQK3Via2pvckRNNXpIakRXYk12ZzA9",
            Base64.getEncoder().encodeToString(encrypted)
        )

        val decrypted = decrypt(encrypted, password)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

}