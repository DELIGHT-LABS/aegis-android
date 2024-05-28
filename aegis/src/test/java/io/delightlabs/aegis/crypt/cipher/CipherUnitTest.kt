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
        val salt = "SALT_1".toByteArray()

        val encrypted = VersionV1().encrypt(secret, password, salt)

        val decrypted = VersionV1().decrypt(encrypted, password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher1_1() {
        val password = "PASSWORD_1".toByteArray()
        val secret = "MESSAGE_1".toByteArray()
        val salt = "SALT_1".toByteArray()

        val decrypted = VersionV1().decrypt("6KcuRTR1lbNhcNBlXbvQmV1lbW3XoPIm7t4q+lpZslLy3mbszg==".toByteArray(), password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher1_2() {
        val password = "PASSWORD_1".toByteArray()
        val secret = "MESSAGE_1".toByteArray()
        val salt = "SALT_1".toByteArray()

        val decrypted = VersionV1().decrypt("U0vFcOsKI+2zAET9K6Qj6pAwpoRwTXmsMFgDfGuZo5E+0Kecfg==".toByteArray(), password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher1_3() {
        val password = "PASSWORD_1".toByteArray()
        val secret = "MESSAGE_1".toByteArray()
        val salt = "SALT_1".toByteArray()

        val decrypted = VersionV1().decrypt("6KcuRTR1lbNhcNBlXbvQmV1lbW3XoPIm7t4q+lpZslLy3mbszg==".toByteArray(), password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher2() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()
        val salt = "SALT_2".toByteArray()

        val encrypted = VersionV1().encrypt(secret, password, salt)

        val decrypted = VersionV1().decrypt(encrypted, password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher2_1() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()
        val salt = "SALT_2".toByteArray()

        val decrypted = VersionV1().decrypt("SRCQlBBY7/oHAliYuyKo+PGSHgG5hsIpKreh1m3XIToZ5uVzUg==".toByteArray(), password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher2_2() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()
        val salt = "SALT_2".toByteArray()

        val decrypted = VersionV1().decrypt("AN5gLlJnG1N8DkI8Nie8tybSkFCrAq0lK/2UB2RYKFG+LkIMgA==".toByteArray(), password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testVersion1Cipher2_3() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()
        val salt = "SALT_2".toByteArray()

        val decrypted = VersionV1().decrypt("HIXTwrwkt8EevwXvCa2XoUaQ2PJJVwUOL0a9EW6hOzvrNGATmA==".toByteArray(), password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testCipher1() {
        val password = "PASSWORD_1".toByteArray()
        val secret = "MESSAGE_1".toByteArray()
        val salt = "SALT_1".toByteArray()

        val encrypted = encrypt(Version.V1, secret, password, salt)

        val decrypted = decrypt(encrypted, password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }

    @Test
    fun testCipher2() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()
        val salt = "SALT_2".toByteArray()

        val encrypted = encrypt(Version.V1, secret, password, salt)

        val decrypted = decrypt(encrypted, password, salt)
        Assert.assertEquals(secret.contentToString(), decrypted.contentToString())
    }
}