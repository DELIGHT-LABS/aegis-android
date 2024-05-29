package io.delightlabs.aegis.protocol

import io.delightlabs.aegis.crypt.NoCryptShare
import io.delightlabs.aegis.protocol.*
import org.junit.Assert
import org.junit.Test

class ProtocolUnitTest {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun `pack & unpack`() {

        val ncs = NoCryptShare(ByteArray(0))
        ncs.content = "VEVTVF9WMV9QQUNLRVRfMTIzNDU2Nzg5MA==".toByteArray()
        ncs.total = 5
        ncs.threshold = 3
        val result = pack(Version.V1, ncs)
        Assert.assertEquals(
            "eyJwcm90b2NvbF92ZXJzaW9uIjoiVjEiLCJwYWNrZXQiOiJleUpqY25sd2RGOWhiR2R2Y21sMGFHMGlPaUpPVDE5RFVsbFFWQ0lzSW5Ob1lYSmxYM0JoWTJ0bGRDSTZJbVY1U2pCaU0xSm9Za05KTms1VGQybGtSMmg1V2xoT2IySXllR3RKYW05NlRFTkthbUl5TlRCYVZ6VXdTV3B2YVZadGRGZFdNVnBIVjJ0a1VGWnRVazlXYlhCelZXeFdWMVpyT1ZWU2EzQllWbGN4WVZSc1drWmlSRnBWWVRGS1YxUlhjekZPYkhBMlZtczFVbFpFUVRWSmJqQTlJbjA9In0="
         , result)

        val unpacked = unpack(result)
        Assert.assertEquals(ncs, unpacked)
    }

}