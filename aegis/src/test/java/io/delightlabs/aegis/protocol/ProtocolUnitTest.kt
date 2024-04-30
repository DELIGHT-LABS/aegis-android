package io.delightlabs.aegis.protocol

import io.delightlabs.aegis.crypt.NoCryptShare
import io.delightlabs.aegis.protocol.*
import org.junit.Assert
import org.junit.Test

class ProtocolUnitTest {

    @Test
    fun `pack & unpack`() {

        val ncs = NoCryptShare("TEST_V1_PACKET_1234567890".toByteArray())
        val result = pack(Version.V1, ncs)
        Assert.assertEquals(149, result.size)

        val unpacked = unpack(result)
        Assert.assertEquals(ncs, unpacked)
    }

}