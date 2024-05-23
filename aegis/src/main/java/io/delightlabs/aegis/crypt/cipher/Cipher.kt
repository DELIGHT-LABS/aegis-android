package io.delightlabs.aegis.crypt.cipher

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.common.Secret
import java.lang.Exception

enum class Version {
    UNSPECIFIED,
    V1
}

const val versionHeaderLen = 16

@RequiresApi(Build.VERSION_CODES.O)
fun encrypt(version: Version, plainText: Secret, password: ByteArray): Secret {
    var encrypted: Secret

    when (version) {
        Version.V1 -> {
            encrypted = VersionV1().encrypt(plainText, password)
        }
        else -> throw Exception("unsupported cipher version")
    }

    // XXX: append cipher version
    val v = ByteArray(versionHeaderLen)
    System.arraycopy(version.name.toByteArray(), 0, v, 0, version.name.length)
    encrypted = v + encrypted

    return encrypted
}

@RequiresApi(Build.VERSION_CODES.O)
fun decrypt(cipherText: Secret, password: ByteArray): Secret {
    // XXX: detach cipher version
    val version = Version.valueOf(cipherText.sliceArray(0 until versionHeaderLen).toString(Charsets.UTF_8).trimEnd('\u0000'))
    val encrypted = cipherText.sliceArray(versionHeaderLen until cipherText.size)

    var decrypted: Secret

    when (version) {
        Version.V1 -> {
            decrypted = VersionV1().decrypt(encrypted, password)
        }
        else -> throw Exception("unsupported cipher version")
    }

    return decrypted
}