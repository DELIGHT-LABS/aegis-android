package io.delightlabs.aegis.crypt.cipher

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.crypt.cipher.aes.Aes
import io.delightlabs.aegis.crypt.cipher.hash.blake2b
import java.security.SecureRandom

class VersionV1 {

    companion object {
        const val aesKeyLen = 32
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(plainText: Secret, password: ByteArray, salt: ByteArray): Secret {
        // Prepare key
        val hashedKey = blake2b(aesKeyLen, password + salt)

        // Encrypt
        val encrypted = Aes.encryptGCM(plainText, hashedKey)
        return encrypted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(cipherText: Secret, password: ByteArray, salt: ByteArray): Secret {
        // Prepare key
        val hashedKey = blake2b(aesKeyLen, password + salt)

        // Decrypt
        return Aes.decryptGCM(cipherText, hashedKey)
    }
}