package io.delightlabs.aegis.crypt.aes

import org.junit.Assert
import org.junit.Test

import java.util.*
import javax.crypto.BadPaddingException

class AesTest {

    @Test
    fun `encrypt and decrypt with valid key returns original data`() {
        val originalData = "Hello, World!".toByteArray()
        val key = "This is a key".toByteArray()

        val encryptedData = Aes.encrypt(originalData, key)
        Assert.assertNotEquals(originalData, encryptedData)

        val decryptedData = Aes.decrypt(encryptedData, key)
        Assert.assertArrayEquals(originalData, decryptedData)
    }

    @Test
    fun `decrypt with invalid key throws exception`() {
        val originalData = "Hello, World!".toByteArray()
        val key = "This is a key".toByteArray()
        val wrongKey = "This is a wrong key".toByteArray()

        val encryptedData = Aes.encrypt(originalData, key)

        Assert.assertThrows(BadPaddingException::class.java) {
            Aes.decrypt(encryptedData, wrongKey)
        }
    }

    @Test
    fun `encrypt and decrypt with empty data returns empty data`() {
        val originalData = "".toByteArray()
        val key = "This is a key".toByteArray()

        val encryptedData = Aes.encrypt(originalData, key)
        Assert.assertNotEquals(originalData, encryptedData)

        val decryptedData = Aes.decrypt(encryptedData, key)
        Assert.assertArrayEquals(originalData, decryptedData)
    }

    @Test
    fun `encrypt and decrypt with large data returns original data`() {
        val originalData = ByteArray(1024 * 1024) // 1MB
        Random().nextBytes(originalData)
        val key = "This is a key".toByteArray()

        val encryptedData = Aes.encrypt(originalData, key)
        Assert.assertNotEquals(originalData, encryptedData)

        val decryptedData = Aes.decrypt(encryptedData, key)
        Assert.assertArrayEquals(originalData, decryptedData)
    }

    @Test
    fun `AES decryption`() {
        val plainText = byteArrayOf(0)

        val key = byteArrayOf(1,2,3,4)

        val encryptedData = Aes.encrypt(plainText, key)
        Assert.assertNotEquals(plainText, encryptedData)
        Assert.assertEquals(ubyteArrayOf(
            112.toUByte(),
            55.toUByte(),
            86.toUByte(),
            99.toUByte(),
            81.toUByte(),
            65.toUByte(),
            99.toUByte(),
            122.toUByte(),
            120.toUByte(),
            71.toUByte(),
            110.toUByte(),
            106.toUByte(),
            101.toUByte(),
            53.toUByte(),
            68.toUByte(),
            106.toUByte(),
            79.toUByte(),
            48.toUByte(),
            99.toUByte(),
            80.toUByte(),
            68.toUByte(),
            53.toUByte(),
            80.toUByte(),
            76.toUByte(),
            116.toUByte(),
            88.toUByte(),
            53.toUByte(),
            80.toUByte(),
            71.toUByte(),
            57.toUByte(),
            51.toUByte(),
            47.toUByte(),
            98.toUByte(),
            89.toUByte(),
            111.toUByte(),
            72.toUByte(),
            107.toUByte(),
            67.toUByte(),
            56.toUByte(),
            56.toUByte(),
            66.toUByte(),
            108.toUByte(),
            103.toUByte(),
            61.toUByte()
        ).contentToString(), encryptedData.toUByteArray().contentToString())

        val decryptedData = Aes.decrypt(encryptedData, key)
        Assert.assertArrayEquals(plainText, decryptedData)
    }

    @Test
    fun `AES decryption2`() {
        val plainText = byteArrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16)

        val key = byteArrayOf(1,2,3,4)

        val encryptedData = Aes.encrypt(plainText, key)
        Assert.assertNotEquals(plainText, encryptedData)

        println(encryptedData.contentToString())

        val decryptedData = Aes.decrypt(encryptedData, key)
        Assert.assertArrayEquals(plainText, decryptedData)
    }
}