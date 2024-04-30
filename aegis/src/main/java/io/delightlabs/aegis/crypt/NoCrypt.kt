package io.delightlabs.aegis.crypt

import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.common.Share
import io.delightlabs.aegis.common.ThresholdAlgorithm

class NoCryptShare(val content: ByteArray) : Share{
    override fun serialize(): ByteArray {
        return content
    }

    override fun getAlgorithm(): Algorithm {
        return NoCrypt().getName()
    }

    override fun equals(other: Any?): Boolean {
        return other is NoCryptShare && content.contentEquals(other.content)
    }
}

class NoCrypt: ThresholdAlgorithm {
    override fun getName(): Algorithm {
        return Algorithm.NO_CRYPT
    }

    override fun dealShares(secret: Secret, threshold: Int, total: Int): List<Share> {
        val ncShare = NoCryptShare(secret)

        return List(total){ncShare}
    }

    override fun combineShares(shares: List<Share>, threshold: Int, total: Int): Secret {
        if (shares.size < threshold) {
            return ByteArray(0)
        }

        val ncShares = shares.filterIsInstance<NoCryptShare>()

        if (ncShares.size < threshold) {
            return ByteArray(0)
        }

        return ncShares.get(0).content
    }
}

