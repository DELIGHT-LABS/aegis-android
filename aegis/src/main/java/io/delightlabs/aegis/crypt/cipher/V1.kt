package io.delightlabs.aegis.crypt.cipher

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.crypt.cipher.aes.Aes
import io.delightlabs.aegis.crypt.cipher.hash.blake2b
import java.lang.Exception

class VersionV1 {

    companion object {
        const val aesKeyLen = 32
        const val aesIvKeyLen = 16
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(plainText: Secret, password: ByteArray): Secret {
        // Prepare key
        val hashedKey = blake2b(aesKeyLen, password)

        // Prepare IvKey
        val ivKey = blake2b(aesIvKeyLen, password)

        // Encrypt
        return Aes.encrypt(plainText, hashedKey, ivKey)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(cipherText: Secret, password: ByteArray): Secret {
        // Prepare key
        val hashedKey = blake2b(aesKeyLen, password)

        // Prepare IvKey
        val ivKey = blake2b(aesIvKeyLen, password)

        // Decrypt
        return Aes.decrypt(cipherText, hashedKey, ivKey)
    }
}