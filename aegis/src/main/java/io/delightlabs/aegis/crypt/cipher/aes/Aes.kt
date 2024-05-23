package io.delightlabs.aegis.crypt.cipher.aes
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Aes {
    const val HMAC_LEN = 16

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(plainText: ByteArray, key: ByteArray, ivKey: ByteArray): ByteArray {
        val hmacText = ivKey + plainText

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(ivKey))
        val paddedPlainText = padPKCS7(hmacText, cipher.blockSize)

        val encrypted = cipher.doFinal(paddedPlainText)

        val sliceEncrypted = encrypted.dropLast(HMAC_LEN).toByteArray()


        return Base64.getEncoder().encode(sliceEncrypted)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(cipherText: ByteArray, key: ByteArray, ivKey: ByteArray): ByteArray {
        val decoded = Base64.getDecoder().decode(cipherText)

        val decipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        decipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(ivKey))

        val data = decipher.update(decoded)
        val decryptedText = data + decipher.doFinal()

        if (!(ivKey.contentEquals(decryptedText.take(HMAC_LEN).toByteArray()))) {
            throw IllegalArgumentException("Wrong key")
        }

        return decryptedText.slice(IntRange(HMAC_LEN, decryptedText.size-1)).toByteArray()
    }

    private fun padPKCS7(plainText: ByteArray, blockSize: Int): ByteArray {
        val padding = blockSize - (plainText.size % blockSize)
        val padText = ByteArray(padding) { padding.toByte() }
        return plainText + padText
    }

   private fun trimPKCS5(text: ByteArray): ByteArray {
        val padding = text.last()
        if (padding.toInt() > text.size) {
            return text
        }
    return text.copyOfRange(0, text.size - padding.toInt())
}

}