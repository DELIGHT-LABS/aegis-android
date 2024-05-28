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
    fun testVersion1Cipher2() {
        val password = "PASSWORD_2".toByteArray()
        val secret = "MESSAGE_2".toByteArray()
        val salt = "SALT_2".toByteArray()

        val encrypted = VersionV1().encrypt(secret, password, salt)

        val decrypted = VersionV1().decrypt(encrypted, password, salt)
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