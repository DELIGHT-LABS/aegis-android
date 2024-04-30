package io.delightlabs.aegis.crypt.hash

import com.appmattus.crypto.Algorithm

fun blake2b(size: Int, key: ByteArray): ByteArray {
    // Create a digest
    val digest = Algorithm.Blake2b.Keyed(key, null, null, size * 8).createDigest()

    // Update the digest with data and generate the hash
    return digest.digest()
}