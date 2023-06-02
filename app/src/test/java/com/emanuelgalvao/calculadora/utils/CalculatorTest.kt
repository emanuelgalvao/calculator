package com.emanuelgalvao.calculadora.utils

import com.emanuelgalvao.calculadora.utils.Calculator
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class CalculatorTest {

    private lateinit var sut: Calculator

    @Before
    fun setup() {
        sut = Calculator()
    }

    @Test
    fun `test sum method should return correct value to different scenarios`() {
        Assert.assertEquals(2.0, sut.sum(1, 1), 0.1)
        Assert.assertEquals(-5.0, sut.sum(-6, 1), 0.1)
        Assert.assertEquals(-7.2, sut.sum(-2.1, -5.1), 0.1)
        Assert.assertEquals(22.4, sut.sum(12.4, 10), 0.1)
        Assert.assertEquals(4565.6, sut.sum(4000.2, 565.4), 0.1)
    }

    @Test
    fun `test minus method should return correct value to different scenarios`() {
        Assert.assertEquals(0.0, sut.minus(1, 1), 0.1)
        Assert.assertEquals(-7.0, sut.minus(-6, 1), 0.1)
        Assert.assertEquals(3.0, sut.minus(-2.1, -5.1), 0.1)
        Assert.assertEquals(2.4, sut.minus(12.4, 10), 0.1)
        Assert.assertEquals(3434.8, sut.minus(4000.2, 565.4), 0.1)
    }

    @Test
    fun `test multiply method should return correct value to different scenarios`() {
        Assert.assertEquals(1.0, sut.multiply(1, 1), 0.1)
        Assert.assertEquals(-6.0, sut.multiply(-6, 1), 0.1)
        Assert.assertEquals(10.71, sut.multiply(-2.1, -5.1), 0.1)
        Assert.assertEquals(124.0, sut.multiply(12.4, 10), 0.1)
        Assert.assertEquals(2261713.08, sut.multiply(4000.2, 565.4), 0.1)
    }

    @Test
    fun `test divide method should return correct value to different scenarios`() {
        Assert.assertEquals(1.0, sut.divide(1, 1), 0.1)
        Assert.assertEquals(-6.0, sut.divide(-6, 1), 0.1)
        Assert.assertEquals(0.4117647059, sut.divide(-2.1, -5.1), 0.1)
        Assert.assertEquals(1.24, sut.divide(12.4, 10), 0.1)
        Assert.assertEquals(7.0749911567, sut.divide(4000.2, 565.4), 0.1)
    }
}