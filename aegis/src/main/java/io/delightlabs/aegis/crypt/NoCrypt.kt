package io.delightlabs.aegis.crypt

import com.google.gson.Gson
import io.delightlabs.aegis.common.NUM_MINIMUM_SHARE
import io.delightlabs.aegis.common.Secret
import io.delightlabs.aegis.common.Share
import io.delightlabs.aegis.common.ThresholdAlgorithm
import io.delightlabs.aegis.protocol.gson

class NoCryptShare(obj: ByteArray) : Share{
    var total: Int = 0
    var threshold: Int = 0
    var content: ByteArray = ByteArray(0)

    init {
        if(obj.isNotEmpty()) {
            gson.fromJson(String(obj), NoCryptShare::class.java).let {
                total = it.total
                threshold = it.threshold
                content = it.content
            }
        }
    }

    override fun serialize(): ByteArray {
        return gson.toJson(this).toByteArray()
    }

    override fun getAlgorithm(): Algorithm {
        return NoCrypt().getName()
    }

    override fun equals(other: Any?): Boolean {
        return other is NoCryptShare
                && total == other.total
                && threshold == other.threshold
                && content.contentEquals(other.content)
    }
}

class NoCrypt: ThresholdAlgorithm {
    override fun getName(): Algorithm {
        return Algorithm.NO_CRYPT
    }

    override fun dealShares(secret: Secret, threshold: Int, total: Int): List<Share> {
        val ncShare = NoCryptShare(ByteArray(0))
        ncShare.content = secret
        ncShare.threshold = threshold
        ncShare.total = total

        return List(total){ncShare}
    }

    override fun combineShares(shares: List<Share>): Secret {
        if (shares.size < NUM_MINIMUM_SHARE) {
            throw Exception("Not enough shares")
        }

        val ncShares = shares.filterIsInstance<NoCryptShare>()

        if (ncShares.isEmpty() || shares.size < ncShares[0].threshold) {
            throw Exception("Not enough shares")
        }

        return ncShares.get(0).content
    }
}

