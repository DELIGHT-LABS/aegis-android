package io.delightlabs.aegis.crypt.cipher

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.crypt.cipher.aes.Aes
import io.delightlabs.aegis.crypt.cipher.hash.blake2b
import java.lang.Exception
import java.security.SecureRandom

class VersionV1 {

    companion object {
        const val aesKeyLen = 32
        const val aesIvKeyLen = 16
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(plainText: Secret, password: ByteArray, salt: ByteArray): Secret {
        // Prepare key
        val hashedKey = blake2b(aesKeyLen, password + salt)

        // Prepare initial vector
        val iv = ByteArray(aesIvKeyLen)
        val random = SecureRandom()
        random.nextBytes(iv)

        // Encrypt
        val encrypted = iv + Aes.encrypt(plainText, hashedKey, iv)
        return encrypted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(cipherText: Secret, password: ByteArray, salt: ByteArray): Secret {
        // Prepare key
        val hashedKey = blake2b(aesKeyLen, password + salt)

        // Detach initial vector
        val iv = cipherText.sliceArray(0 until aesIvKeyLen)
        val encrypted = cipherText.sliceArray(aesIvKeyLen until cipherText.size)

        // Decrypt
        return Aes.decrypt(encrypted, hashedKey, iv)
    }
}