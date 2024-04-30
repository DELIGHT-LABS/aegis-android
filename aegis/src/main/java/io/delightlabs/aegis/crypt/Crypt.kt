package io.delightlabs.aegis.crypt

import com.google.gson.Gson
import io.delightlabs.aegis.common.Share
import io.delightlabs.aegis.common.ThresholdAlgorithm
import java.lang.Exception

enum class Algorithm {
    UNSPECIFIED,
    NO_CRYPT,
    TSED25519_V1;

    fun new(): ThresholdAlgorithm {
        return when (this) {
            NO_CRYPT -> NoCrypt()
            TSED25519_V1 -> Ed25519ThresholdV0()
            else -> throw Exception("Unsupported algorithm")
        }
    }

    fun newShare(content: ByteArray): Share {
        return when (this) {
            NO_CRYPT -> NoCryptShare(content)
            TSED25519_V1 -> {
                val ed25519tshare: Ed25519ThresholdV0Share = Gson().fromJson(String(content), Ed25519ThresholdV0Share::class.java)
                Ed25519ThresholdV0Share(ed25519tshare.index, ed25519tshare.parts)
            }
            else -> throw Exception("Unsupported crypt algorithm")
        }
    }
}