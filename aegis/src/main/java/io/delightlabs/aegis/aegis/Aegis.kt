package io.delightlabs.aegis

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.citadel.Citadel
import io.delightlabs.aegis.common.NUM_MINIMUM_SHARE
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.common.Share
import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.crypt.cipher.Version as CipherVersion
import io.delightlabs.aegis.crypt.cipher.aes.Aes
import io.delightlabs.aegis.crypt.cipher.decrypt
import io.delightlabs.aegis.crypt.cipher.encrypt
import io.delightlabs.aegis.protocol.Payload
import io.delightlabs.aegis.protocol.Version as ProtocolVersion
import io.delightlabs.aegis.protocol.pack
import io.delightlabs.aegis.protocol.unpack


typealias payload = ByteArray
class Aegis (threshold: Int, total: Int) {
    var threshold: Int = 0
    var total: Int = 0

    init {
        this.threshold = threshold
        this.total = total
    }

    var payloads: List<payload> = emptyList()

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun dealShares(
            pVersion: ProtocolVersion,
            cVersion: CipherVersion,
            algorithm: Algorithm,
            threshold: Int,
            total: Int,
            secret: Secret,
            password: ByteArray
        ): Aegis {
            val aegis = Aegis(threshold, total)

            if (threshold < NUM_MINIMUM_SHARE) {
                throw Exception("too low threshold")
            }

            // Encrypt
            val encrypted = encrypt(cVersion, secret, password)

            // Deal
            val algo = algorithm.new()

            val shares = algo.dealShares(encrypted, threshold, total)

            // Verify
            val combined = algo.combineShares(shares)
            if (!encrypted.contentEquals(combined)) {
                throw Exception("shares verification failed")
            }

            // Pack
            shares.forEach { share ->
                val packed = pack(pVersion, share)
                aegis.payloads = aegis.payloads.plus(packed)
            }

            return aegis
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun combineShares(password: ByteArray): Secret {
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
        val combined = algo.combineShares(shares)

        // Decrypt
        return decrypt(combined, password)
    }
}