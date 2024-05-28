package io.delightlabs.aegis.crypt.cipher.aes

import org.junit.Assert
import org.junit.Test

class AesTest {

    @Test
    fun `AES decryption`() {
        val key = "01234567890123456789012345678901".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val encryptedData = Aes.encryptGCM(secret, key)
        Assert.assertNotEquals(secret, encryptedData)

        val decryptedData = Aes.decryptGCM(encryptedData, key)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption2`() {
        val key = "012345678901234567890123456789ab".toByteArray()
        val ivKey = "0123456789abcdef".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val encryptedData = Aes.encryptGCM(secret, key)
        Assert.assertNotEquals(secret, encryptedData)

        val decryptedData = Aes.decryptGCM(encryptedData, key)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption test 1-1`() {
        val key = "01234567890123456789012345678901".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val decryptedData = Aes.decryptGCM("cG/ezZh4VfsYDHPrMKMMDJdbYHGb0XAqVyJdo3QSS22mws7CIA==".toByteArray(), key)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption test 1-2`() {
        val key = "01234567890123456789012345678901".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val decryptedData = Aes.decryptGCM("FCZwCSdf27zY/mLoc74+VFDvb7s/fln73VaV9WKNyLUPwdd0Dg==".toByteArray(), key)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption test 1-3`() {
        val key = "01234567890123456789012345678901".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val decryptedData = Aes.decryptGCM("1Ugg5/Z58la6IOzhEXLgP77AwQ7iAvwbJm7H6URDyWyhuKFyQw==".toByteArray(), key)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption test 2-1`() {
        val key = "012345678901234567890123456789ab".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val decryptedData = Aes.decryptGCM("GX8i6t3yKtAIDow+V6QwLhHh2iR9war0EJxZIAWcMclaTfAf5A==".toByteArray(), key)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption test 2-2`() {
        val key = "012345678901234567890123456789ab".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val decryptedData = Aes.decryptGCM("uF4MuvoNJun6Oemw7aP96JNUNhUo2pfoKdFHaQkFy92sVFDinA==".toByteArray(), key)
        Assert.assertArrayEquals(secret, decryptedData)
    }

    @Test
    fun `AES decryption test 2-3`() {
        val key = "012345678901234567890123456789ab".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val decryptedData = Aes.decryptGCM("5pEp+ZztChtQ4rqYTjGBLnLPQbRcW/03fI8mb3DjxV7PBWf9Fg==".toByteArray(), key)
        Assert.assertArrayEquals(secret, decryptedData)
    }
}
