package io.delightlabs.aegis.protocol

import com.google.gson.annotations.SerializedName
import io.delightlabs.aegis.common.Packet
import io.delightlabs.aegis.crypt.Algorithm
import io.delightlabs.aegis.common.Share
import java.lang.Exception

class VersionV1 : Protocol {
    @SerializedName("crypt_algorithm")
    var cryptAlgorithm: String? = null

    @SerializedName("share_packet")
    var sharePacket: ByteArray? = null

    @Transient
    var share: Share? = null

    override fun getVersion(): Version {
        return Version.V1
    }

    override fun pack(v: Any): Packet {
        if (v !is Share) {
            throw Exception("Protocol argument mismatch")
        }

        this.cryptAlgorithm = v.getAlgorithm().name
        this.sharePacket = v.serialize()

        return gson.toJson(this).toByteArray()
    }

    override fun unpack(packet: Packet): Any {
        val v1 = gson.fromJson(String(packet), VersionV1::class.java)

        this.share = v1.cryptAlgorithm?.let { algo ->
            v1.sharePacket?.let { packet ->
                Algorithm.valueOf(algo).newShare(packet)
            }
        } ?: throw Exception("Invalid data format")

        return this.share ?: throw Exception("Invalid data format")
    }
}

