package io.delightlabs.aegis.crypt.cipher.hash

import com.appmattus.crypto.Algorithm

fun blake2b(size: Int, key: ByteArray): ByteArray {
    // Create a digest
    val digest = Algorithm.Blake2b(size * 8).createDigest()

    // Update the digest with data and generate the hash
    digest.update(key)
    return digest.digest()
}