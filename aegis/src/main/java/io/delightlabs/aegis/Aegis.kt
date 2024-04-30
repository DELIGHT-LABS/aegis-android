package io.delightlabs.aegis

import android.os.Build
import androidx.annotation.RequiresApi
import io.delightlabs.aegis.citadel.Citadel
import io.delightlabs.aegis.common.NUM_MINIMUM_SHARE
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.common.Share
import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.crypt.aes.Aes
import io.delightlabs.aegis.protocol.Payload
import io.delightlabs.aegis.protocol.Version
import io.delightlabs.aegis.protocol.pack
import io.delightlabs.aegis.protocol.unpack


typealias payload = ByteArray
class Aegis(
    private val protocolVersion: Version,
    private val threshold: Int,
    private val total: Int,
) {
    var payloads: List<payload> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    fun dealShares(algorithm: Algorithm, secret: Secret, password: ByteArray) {
        // Encrypt
        val encrypted = Aes.encrypt(secret, password)

        // Deal
        val algo = algorithm.new()

        val shares = algo.dealShares(encrypted, threshold, total)

        // Verify
        val combined = algo.combineShares(shares, threshold, total)
        if (!encrypted.contentEquals(combined)) {
            throw Exception("shares verification failed")
        }

        // Pack
        shares.forEach { share ->
            val packed = pack(protocolVersion, share)
            payloads = payloads.plus(packed)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun combineShares(password: ByteArray): Secret {
        // Pre-verification
        if (payloads.size < NUM_MINIMUM_SHARE) {
            throw Exception("not enough shares")
        }

        // Unpack
        var algorithm = Algorithm.UNSPECIFIED

        var shares = emptyList<Share>()
        payloads.forEach { payload ->
            var share = unpack(payload)
            if(share !is Share) {
                throw Exception("Protocol argument mismatch")
            }
            share = share as Share
            shares = shares.plus(share)

            if (algorithm == Algorithm.UNSPECIFIED) {
                algorithm = share.getAlgorithm()
            } else if (algorithm != share.getAlgorithm()) {
                throw Exception("algorithm mismatch")
            }
        }

        // Combine
        val algo = algorithm.new()
        val combined = algo.combineShares(shares, threshold, total)

        // Decrypt
        return Aes.decrypt(combined, password)
    }

    fun protect(shares: List<Share>, vararg keys: String) {
        // TODO: implementation
    }

    fun unprotect(vararg keys: String): List<ByteArray>? {
        // TODO: implementation
        return null
    }

    companion object {
        fun new(protocolVersion: Version, threshold: Int, total: Int): Aegis {
            return Aegis(protocolVersion, threshold, total)
        }

        fun newWithDefault(threshold: Int, total: Int): Aegis {
            return new(Version.V1, threshold, total)
        }
    }
}