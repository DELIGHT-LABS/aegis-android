package io.delightlabs.aegis.crypt.cipher.hash

import org.junit.Assert
import org.junit.Test
import java.util.Base64

class Blake2bUnitTest {
    @Test
    fun testBlake2b() {
        var res = blake2b(16, "MESSAGE_1".toByteArray())
        Assert.assertEquals(
           "Hic2zt1El2Y8DP9nWU7J7Q==",
            Base64.getEncoder().encodeToString(res)
        )

        res = blake2b(32, "MESSAGE_1".toByteArray())
        Assert.assertEquals(
            "U0l/Dirpdm8S7d9YhObO+UjaXaQhSf16px09BCVG+U0=",
            Base64.getEncoder().encodeToString(res)
        )

        res = blake2b(16, "MESSAGE_2".toByteArray())
        Assert.assertEquals(
            "uFTfD9lzYLh2+JU/bftzLw==",
            Base64.getEncoder().encodeToString(res)
        )

        res = blake2b(32, "MESSAGE_2".toByteArray())
        Assert.assertEquals(
            "SQnar3aTns+q+THbN5LrcTYHZfdJs/GCu1CejmwHbcE=",
            Base64.getEncoder().encodeToString(res)
        )
    }

    @Test
    fun testChecksum() {
        var res = checkSum("TEST1")
        Assert.assertEquals(
            "6e2323ffbd238097ea541302a5b15d40ea23f9cd69d3664a6b7b07f6d0dc87f04d4534b03764d67c2b69dcbe4743bc0ad91082d54a139d4095920865d7216eda",
            res
        )

        res = checkSum("TEST2")
        Assert.assertEquals(
            "e97c7ee5e87d89409a2072f0a7fdadd7abc8aa6eb13d5f94465ab418ebf35f92d92d8797275149d5efa4fe08da656a8fc737daab0f75a726cac39b462857e492",
            res
        )

        res = checkSum("TEST3")
        Assert.assertEquals(
            "57757b849a72bb4c0c0efd58ebb9729296c3eb4630bd5bf16ee9e15c1d37977d3a149a7f763261a341e4335ec57b9e1453958730b52ba0b108d68fa723e55223",
            res
        )
    }
}