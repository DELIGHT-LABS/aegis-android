package io.delightlabs.aegis.crypt

import com.google.gson.Gson
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.common.Share
import io.delightlabs.aegis.common.ThresholdAlgorithm

data class Part(
    val part: ByteArray,
    val length: Int
)

data class Ed25519ThresholdV0Share(
    val index: Int,
    val parts: List<Part>
) : Share {
    override fun getAlgorithm(): Algorithm {
        return Ed25519ThresholdV0().getName()
    }

    override fun serialize(): ByteArray {
        return Gson().toJson(this).toByteArray()
    }
}

class Ed25519ThresholdV0: ThresholdAlgorithm {

    override fun getName(): Algorithm {
        return Algorithm.TSED25519_V1
    }

    override fun dealShares(secret: ByteArray, threshold: Int, total: Int): List<Share> {
        // The implementation of this method is omitted for brevity.
        // You should replace this with your actual implementation.
        return emptyList()
    }

    override fun combineShares(shares: List<Share>, threshold: Int, total: Int): Secret {
        // The implementation of this method is omitted for brevity.
        // You should replace this with your actual implementation.
        return ByteArray(0)
    }
}