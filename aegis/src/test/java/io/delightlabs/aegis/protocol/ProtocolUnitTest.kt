package io.delightlabs.aegis.protocol

import io.delightlabs.aegis.crypt.NoCryptShare
import io.delightlabs.aegis.protocol.*
import org.junit.Assert
import org.junit.Test

class ProtocolUnitTest {

    val ncs = NoCryptShare(ByteArray(0))

    init {
        ncs.content = "VEVTVF9WMV9QQUNLRVRfMTIzNDU2Nzg5MA==".toByteArray()
        ncs.total = 5
        ncs.threshold = 3
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun `pack & unpack`() {

        val ncs = NoCryptShare(ByteArray(0))
        ncs.content = "VEVTVF9WMV9QQUNLRVRfMTIzNDU2Nzg5MA==".toByteArray()
        ncs.total = 5
        ncs.threshold = 3
        val timestamp = 1723096536082
        val result = pack(Version.V1, ncs, timestamp)
        Assert.assertEquals(
            "eyJ0aW1lc3RhbXAiOjE3MjMwOTY1MzYwODIsInByb3RvY29sX3ZlcnNpb24iOiJWMSIsInBhY2tldCI6ImV5Smpjbmx3ZEY5aGJHZHZjbWwwYUcwaU9pSk9UMTlEVWxsUVZDSXNJbk5vWVhKbFgzQmhZMnRsZENJNkltVjVTakJpTTFKb1lrTkpOazVUZDJsa1IyaDVXbGhPYjJJeWVHdEphbTk2VEVOS2FtSXlOVEJhVnpVd1NXcHZhVlp0ZEZkV01WcEhWMnRrVUZadFVrOVdiWEJ6Vld4V1YxWnJPVlZTYTNCWVZsY3hZVlJzV2taaVJGcFZZVEZLVjFSWGN6Rk9iSEEyVm1zMVVsWkVRVFZKYmpBOUluMD0ifQ=="
         , result)

        val unpacked = unpack(result)
        Assert.assertEquals(ncs, unpacked.first)
        Assert.assertEquals(timestamp, unpacked.second)
    }

    @Test
    fun `compatibility without timestamp` () {
        val packed = "eyJwcm90b2NvbF92ZXJzaW9uIjoiVjEiLCJwYWNrZXQiOiJleUpqY25sd2RGOWhiR2R2Y21sMGFHMGlPaUpPVDE5RFVsbFFWQ0lzSW5Ob1lYSmxYM0JoWTJ0bGRDSTZJbVY1U2pCaU0xSm9Za05KTms1VGQybGtSMmg1V2xoT2IySXllR3RKYW05NlRFTkthbUl5TlRCYVZ6VXdTV3B2YVZadGRGZFdNVnBIVjJ0a1VGWnRVazlXYlhCelZXeFdWMVpyT1ZWU2EzQllWbGN4WVZSc1drWmlSRnBWWVRGS1YxUlhjekZPYkhBMlZtczFVbFpFUVRWSmJqQTlJbjA9In0="
        val unpacked = unpack(packed)
        Assert.assertEquals(ncs, unpacked.first)
        Assert.assertEquals(0, unpacked.second)
    }

}