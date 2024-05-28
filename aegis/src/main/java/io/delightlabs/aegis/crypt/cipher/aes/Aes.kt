package io.delightlabs.aegis.crypt.cipher.aes
import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.common.Secret
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object Aes {
    const val IV_LEN = 12
    const val TAG_LEN = 16

    @OptIn(ExperimentalStdlibApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptGCM(plainText: Secret, key: ByteArray): Secret {
        // Prepare key
        val secretKey: SecretKey = SecretKeySpec(key, "AES")

        // Encrypt
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encrypted = cipher.iv + cipher.doFinal(plainText)
        // Base64 encoding
        return Base64.getEncoder().encode(encrypted)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptGCM(cipherText: Secret, key: ByteArray): Secret {
        val decoded = Base64.getDecoder().decode(cipherText)

        val ivKey = decoded.slice(IntRange(0, IV_LEN-1)).toByteArray()
        val encryptedText = decoded.slice(IntRange(IV_LEN, decoded.size-TAG_LEN-1)).toByteArray()
        val tag = decoded.slice(IntRange(decoded.size-TAG_LEN, decoded.size-1)).toByteArray()

        val decipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(TAG_LEN * 8, ivKey)
        decipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), spec)

        val decryptedText = decipher.doFinal(encryptedText + tag)

        return decryptedText
    }

}