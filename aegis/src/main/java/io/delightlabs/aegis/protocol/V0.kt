package io.delightlabs.aegis.protocol

class VersionV0 : Protocol {
    override fun getVersion(): Version {
        return Version.V0
    }

    override fun pack(v: Any): ByteArray {
        // Implement your packing logic here
        return ByteArray(0)
    }

    override fun unpack(packet: ByteArray): Any {
        // Implement your unpacking logic here
        return Any()
    }
}