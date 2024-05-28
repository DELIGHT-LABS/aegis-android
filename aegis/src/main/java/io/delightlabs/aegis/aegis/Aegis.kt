package io.delightlabs.aegis

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.common.NUM_MINIMUM_SHARE
import io.delightlabs.aegis.common.Packet
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.common.Share
import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.crypt.cipher.Version as CipherVersion
import io.delightlabs.aegis.crypt.cipher.encrypt as cipherEncrypt
import io.delightlabs.aegis.crypt.cipher.decrypt as cipherDecrypt
import io.delightlabs.aegis.protocol.Version as ProtocolVersion
import io.delightlabs.aegis.protocol.pack
import io.delightlabs.aegis.protocol.unpack


typealias payload = ByteArray
class Aegis {
    var payloads: List<payload> = emptyList()

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun dealShares(
            pVersion: ProtocolVersion,
            algorithm: Algorithm,
            threshold: Int,
            total: Int,
            secret: Secret,
        ): Aegis {
            val aegis = Aegis()

            if (threshold < NUM_MINIMUM_SHARE) {
                throw Exception("too low threshold")
            }

            // Deal
            val algo = algorithm.new()

            val shares = algo.dealShares(secret, threshold, total)

            // Verify
            val combined = algo.combineShares(shares)
            if (!secret.contentEquals(combined)) {
                throw Exception("shares verification failed")
            }

            // Pack
            shares.forEach { share ->
                val packed = pack(pVersion, share)
                aegis.payloads = aegis.payloads.plus(packed)
            }

            return aegis
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun combineShares(payloads: List<payload>): Secret {
            // Pre-verification
            if (payloads.isEmpty() || payloads.size < NUM_MINIMUM_SHARE) {
                throw Exception("not enough shares")
            }

            // Unpack
            var algorithm = Algorithm.UNSPECIFIED

            var shares = emptyList<Share>()
            payloads.forEach { payload ->
                val share = unpack(payload)
                if (share !is Share) {
                    throw Exception("Protocol argument mismatch")
                }
                shares = shares.plus(share)

                if (algorithm == Algorithm.UNSPECIFIED) {
                    algorithm = share.getAlgorithm()
                } else if (algorithm != share.getAlgorithm()) {
                    throw Exception("algorithm mismatch")
                }
            }

            // Combine
            val algo = algorithm.new()
            return algo.combineShares(shares)
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun encrypt(cVersion: CipherVersion, secret: Secret, password: ByteArray, salt: ByteArray): Packet {
    val encrypted = cipherEncrypt(cVersion, secret, password, salt)

    // Verify
    val decrypted = cipherDecrypt(encrypted, password, salt)

    if (!decrypted.contentEquals(secret)) {
        throw Exception("Encryption verification failed")
    }

    return encrypted
}

@RequiresApi(Build.VERSION_CODES.O)
fun decrypt(secret: Packet, password: ByteArray, salt: ByteArray): Secret {
    return cipherDecrypt(secret, password, salt)
}