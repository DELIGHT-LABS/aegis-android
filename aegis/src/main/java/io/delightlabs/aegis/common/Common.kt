package io.delightlabs.aegis.common

import io.delightlabs.aegis.crypt.Algorithm

typealias Packet = ByteArray

typealias Secret = ByteArray

interface ThresholdAlgorithm {
    fun getName(): Algorithm
    fun dealShares(secret: Secret, threshold: Int, total: Int): List<Share>
    fun combineShares(shares: List<Share>): Secret
}

interface Share {
    fun getAlgorithm(): Algorithm
    fun serialize(): ByteArray
}

const val NUM_MINIMUM_SHARE = 3