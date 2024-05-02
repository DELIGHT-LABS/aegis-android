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
}