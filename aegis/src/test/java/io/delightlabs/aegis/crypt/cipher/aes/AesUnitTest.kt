package io.delightlabs.aegis.crypt.cipher.aes

import io.delightlabs.aegis.crypt.cipher.aes.Aes
import org.junit.Assert
import org.junit.Test

import java.util.*

class AesTest {

    @Test
    fun `AES decryption`() {
        val key = "01234567890123456789012345678901".toByteArray()
        val ivKey = "0123456789012345".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val encryptedData = Aes.encrypt(secret, key, ivKey)
        Assert.assertNotEquals(secret, encryptedData)
        Assert.assertEquals("OTh1VC9tZ0JZWFI4QVpUbW8xdXI2bW1OR0ZGd3VwREREbjNENmp5WHRmZz0=", Base64.getEncoder().encodeToString(encryptedData))

        val decryptedData = Aes.decrypt(encryptedData, key, ivKey)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption2`() {
        val key = "012345678901234567890123456789ab".toByteArray()
        val ivKey = "0123456789abcdef".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val encryptedData = Aes.encrypt(secret, key, ivKey)
        Assert.assertNotEquals(secret, encryptedData)
        Assert.assertEquals("K1Fac2RkNUZXdkFXYTlFQUk3REx4ZzBIQURWRmYwZmd2RnlnY1ZCRm9YUT0=", Base64.getEncoder().encodeToString(encryptedData))

        val decryptedData = Aes.decrypt(encryptedData, key, ivKey)
        Assert.assertArrayEquals(secret, decryptedData)
    }
}