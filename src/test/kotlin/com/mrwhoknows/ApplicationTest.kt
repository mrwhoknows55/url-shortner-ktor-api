package com.mrwhoknows

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    // TODO: do better job here
    @Test
    fun testRoot() {
        val rand = Random().nextBoolean()
        assertEquals(rand, true)
    }
}
