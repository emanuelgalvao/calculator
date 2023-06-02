package com.emanuelgalvao.calculadora.utils

import org.junit.Assert
import org.junit.Test

internal class StringExtensionsTest {

    @Test
    fun `test removeLastChar method should return correct value for different scenarios`() {
        Assert.assertEquals("", "".removeLastChar())
        Assert.assertEquals("", "a".removeLastChar())
        Assert.assertEquals("Tes", "Test".removeLastChar())
    }

    @Test
    fun `test isCalculatorSymbol method should return corrected value for different scenarios`() {
        Assert.assertEquals(true, "+".isCalculatorSymbol())
        Assert.assertEquals(true, "-".isCalculatorSymbol())
        Assert.assertEquals(true, "*".isCalculatorSymbol())
        Assert.assertEquals(true, "/".isCalculatorSymbol())
        Assert.assertEquals(true, ",".isCalculatorSymbol())
        Assert.assertEquals(false, "2".isCalculatorSymbol())
        Assert.assertEquals(false, "563".isCalculatorSymbol())
    }
}