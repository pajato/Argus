package com.pajato.argus

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class StringExtensionsUnitTest {

    /** Test that stripping white-space from the middle of a string works properly. */
    @Test fun testStripMiddle() {
        val testString = "a      string with              embedded              white-space."
        val actual = testString.stripMiddle()
        val expected = testString.replace(Regex("\\s+"), " ")
        assertEquals("The string was not stripped correctly!", expected, actual)
    }

    /** Test that stripping white-space from the start of a string works properly. */
    @Test fun testStripLeft() {
        val testString = "               a string with leading white-space."
        val actual = testString.stripLeft()
        val expected = testString.replace(Regex("^\\s+"), "")
        assertEquals("The string was not stripped correctly!", expected, actual)
    }

    /** Test that stripping white-space from the end of a string works properly. */
    @Test fun testStripRight() {
        val testString = "a string with trailing white-space.                     "
        val actual = testString.stripRight()
        val expected = testString.replace(Regex("\\s+$"), "")
        assertEquals("The string was not stripped correctly!", expected, actual)
    }

    /** Test that stripping all white-space from a string works properly. */
    @Test fun testStrip() {
        val testString = "   a   string with   leading,   trailing and    embedded    white-space."
        val actual = testString.strip()
        val exp1 = testString.replace(Regex("^\\s+"), "")
        val exp2 = exp1.replace(Regex("\\s+$"), "")
        val expected = exp2.replace(Regex("\\s+"), " ")
        assertEquals("The string was not stripped correctly!", expected, actual)
    }
}
