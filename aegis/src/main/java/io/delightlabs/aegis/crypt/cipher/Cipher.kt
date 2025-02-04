package io.delightlabs.aegis.crypt.cipher

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.common.Packet
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.protocol.gson
import io.delightlabs.aegis.protocol.pack
import io.ktor.http.Url
import java.lang.Exception
import java.util.Base64

enum class Version {
    UNSPECIFIED,
    V1
}

data class CipherPacket (
    val version: Version,
    var cipherText: ByteArray
)

@RequiresApi(Build.VERSION_CODES.O)
fun encrypt(version: Version, plainText: Secret, password: ByteArray, salt: ByteArray): String {
    val packet: CipherPacket

    when (version) {
        Version.V1 -> {
            val encrypted = VersionV1().encrypt(plainText, password, salt)
            packet = CipherPacket(version, encrypted)
        }
        else -> throw Exception("unsupported cipher version")
    }

    val packed = gson.toJson(packet).toByteArray()
    return Base64.getEncoder().encodeToString(packed)
}

@RequiresApi(Build.VERSION_CODES.O)
fun decrypt(packet: String, password: ByteArray, salt: ByteArray): Secret {
    val decoded = Base64.getDecoder().decode(packet)

    val cipher = gson.fromJson(String(decoded), CipherPacket::class.java)

    val decrypted: Secret

    when (cipher.version) {
        Version.V1 -> {
            decrypted = VersionV1().decrypt(cipher.cipherText, password, salt)
        }
        else -> throw Exception("unsupported cipher version")
    }

    return decrypted
}