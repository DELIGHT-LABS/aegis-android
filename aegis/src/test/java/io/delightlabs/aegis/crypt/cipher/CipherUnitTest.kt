package io.delightlabs.aegis.crypt.cipher

import io.delightlabs.aegis.crypt.cipher.hash.blake2b
import org.junit.Assert
import org.junit.Test
import java.util.Base64

class CipherUnitTest {
    @Test
    fun testVersion1Cipher1() {
        val password = "PASSWORD_1".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val encrypted = VersionV1().encrypt(secret, password)
        Assert.assertEquals(
            "SXJTdWlYWjRMOE5DSElEWnpMbGY0RFFxdzNMUGFSQzV0c3pXWTc1RkFPQT0=",
            Base64.getEncoder().encodeToString(encrypted)
        )

        val decrypted = VersionV1().decrypt(encrypted, password)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher2() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val encrypted = VersionV1().encrypt(secret, password)
        Assert.assertEquals(
            "b1RNNHJ4T2hEQmw2VkR6WWdsUitYZVArdWJram9yRE01ekhqRFdiTXZnMD0=",
            Base64.getEncoder().encodeToString(encrypted)
        )

        val decrypted = VersionV1().decrypt(encrypted, password)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testCipher1() {
        val password = "PASSWORD_1".toByteArray()
        val secret = "MESSAGE_1".toByteArray()

        val encrypted = encrypt(Version.V1, secret, password)
        Assert.assertEquals(
            "eyJ2ZXJzaW9uIjoiVjEiLCJjaXBoZXJUZXh0IjoiU1hKVGRXbFlXalJNT0U1RFNFbEVXbnBNYkdZMFJGRnhkek5NVUdGU1F6VjBjM3BYV1RjMVJrRlBRVDA9In0=",
            Base64.getEncoder().encodeToString(encrypted)
        )

        val decrypted = decrypt(encrypted, password)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testCipher2() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()

        val encrypted = encrypt(Version.V1, secret, password)
        Assert.assertEquals(
            "eyJ2ZXJzaW9uIjoiVjEiLCJjaXBoZXJUZXh0IjoiYjFSTk5ISjRUMmhFUW13MlZrUjZXV2RzVWl0WVpWQXJkV0pyYW05eVJFMDFla2hxUkZkaVRYWm5NRDA9In0=",
            Base64.getEncoder().encodeToString(encrypted)
        )

        val decrypted = decrypt(encrypted, password)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }
}