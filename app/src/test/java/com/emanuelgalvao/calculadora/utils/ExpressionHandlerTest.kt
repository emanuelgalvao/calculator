package com.emanuelgalvao.calculadora.utils

import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class ExpressionHandlerTest {

    private lateinit var sut: ExpressionHandler

    @Before
    fun setup() {
        sut = ExpressionHandler()
    }

    @Test
    fun `test resolve method should return correct value to different scenarios`() {
        Assert.assertEquals(12.0, sut.resolve("10+2"), 0.1)
        Assert.assertEquals(7.0, sut.resolve("10+2-5"), 0.1)
        Assert.assertEquals(-8.0, sut.resolve("10+2-20"), 0.1)
        Assert.assertEquals(-8.0, sut.resolve("-10+2"), 0.1)
        Assert.assertEquals(50.0, sut.resolve("10+2*20"), 0.1)
        Assert.assertEquals(55.0, sut.resolve("10+2*20+5"), 0.1)
        Assert.assertEquals(52.5, sut.resolve("10+2*20+5/2"), 0.1)
        Assert.assertEquals(32.5, sut.resolve("-10+2*20+5/2"), 0.1)
        Assert.assertThrows(ExpressionException::class.java) { sut.resolve("-*") }
    }

    @Test
    fun `test resolveNegativeSignalAtExpressionStart method should remove signal at start of expression`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("", "-", "1", "+", "2", "-", "3"))

        val resolveNegativeSignalAtExpressionStartMethod = sut.javaClass.getDeclaredMethod("resolveNegativeSignalAtExpressionStart")
        resolveNegativeSignalAtExpressionStartMethod.isAccessible = true
        resolveNegativeSignalAtExpressionStartMethod.invoke(sut)

        Assert.assertEquals(mutableListOf("-1", "+", "2", "-", "3"), expressionSplitField.get(sut))
    }

    @Test
    fun `test hasNegativeSignalAtExpressionStart method should return false when no has negative signal at start`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("1", "+", "2", "-", "3"))

        val hasNegativeSignalAtExpressionStartMethod = sut.javaClass.getDeclaredMethod("hasNegativeSignalAtExpressionStart")
        hasNegativeSignalAtExpressionStartMethod.isAccessible = true

        Assert.assertEquals(false, hasNegativeSignalAtExpressionStartMethod.invoke(sut))
    }

    @Test
    fun `test hasNegativeSignalAtExpressionStart method should return true when has negative signal at start`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("", "-", "1", "+", "2", "-", "3"))

        val hasNegativeSignalAtExpressionStartMethod = sut.javaClass.getDeclaredMethod("hasNegativeSignalAtExpressionStart")
        hasNegativeSignalAtExpressionStartMethod.isAccessible = true

        Assert.assertEquals(true, hasNegativeSignalAtExpressionStartMethod.invoke(sut))
    }

    @Test
    fun `test hasExpressionToResolve method should return false when no has more expression`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("2.0"))

        val hasExpressionToResolveMethod = sut.javaClass.getDeclaredMethod("hasExpressionToResolve")
        hasExpressionToResolveMethod.isAccessible = true

        Assert.assertEquals(false, hasExpressionToResolveMethod.invoke(sut))
    }

    @Test
    fun `test hasExpressionToResolve method should return true when has more expression`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("2", "*", "5"))

        val hasExpressionToResolveMethod = sut.javaClass.getDeclaredMethod("hasExpressionToResolve")
        hasExpressionToResolveMethod.isAccessible = true

        Assert.assertEquals(true, hasExpressionToResolveMethod.invoke(sut))
    }

    @Test
    fun `test getOperatorPosition method should return position of multiply symbol when has multiply symbol`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("5", "+", "7", "-", "2", "*", "5"))

        val getOperatorPositionMethod = sut.javaClass.getDeclaredMethod("getOperatorPosition")
        getOperatorPositionMethod.isAccessible = true

        Assert.assertEquals(5, getOperatorPositionMethod.invoke(sut))
    }

    @Test
    fun `test getOperatorPosition method should return position of sum symbol when no has multiply or division symbol`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("5", "+", "7", "-", "2", "+", "12"))

        val getOperatorPositionMethod = sut.javaClass.getDeclaredMethod("getOperatorPosition")
        getOperatorPositionMethod.isAccessible = true

        Assert.assertEquals(1, getOperatorPositionMethod.invoke(sut))
    }

    @Test
    fun `test resolveOperation method should return correct value to different scenarios`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("5", "+", "7", "-", "2", "+", "12", "/", "5", "+", "7", "-", "2", "*", "5"))

        val resolveOperationMethod = sut.javaClass.getDeclaredMethod("resolveOperation", Int::class.java)
        resolveOperationMethod.isAccessible = true

        Assert.assertEquals(12.0, resolveOperationMethod.invoke(sut, 1))
        Assert.assertEquals(5.0, resolveOperationMethod.invoke(sut, 3))
        Assert.assertEquals(14.0, resolveOperationMethod.invoke(sut, 5))
        Assert.assertEquals(2.4, resolveOperationMethod.invoke(sut, 7))
        Assert.assertEquals(12.0, resolveOperationMethod.invoke(sut, 9))
        Assert.assertEquals(10.0, resolveOperationMethod.invoke(sut, 13))
    }

    @Test
    fun `test calculate method should return correct value to different scenarios`() {
        val calculateMethod = sut.javaClass.getDeclaredMethod("calculate", String::class.java, String::class.java, String::class.java)
        calculateMethod.isAccessible = true

        Assert.assertEquals(3.0, calculateMethod.invoke(sut, "+", "1", "2"))
        Assert.assertEquals(45.3, calculateMethod.invoke(sut, "-", "50", "4.7"))
        Assert.assertEquals(5.0, calculateMethod.invoke(sut, "/", "15", "3"))
        Assert.assertEquals(36.0, calculateMethod.invoke(sut, "*", "12", "3"))
        Assert.assertEquals(0.0, calculateMethod.invoke(sut, "&", "1", "2"))
    }

    @Test
    fun `test removeResolvedOperation method should remove operation after revolved`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("12", "/", "5", "+", "7", "-", "2", "*", "5"))

        val removeResolvedOperationMethod = sut.javaClass.getDeclaredMethod("removeResolvedOperation", Int::class.java, Double::class.java)
        removeResolvedOperationMethod.isAccessible = true

        removeResolvedOperationMethod.invoke(sut, 1, 2.4)
        Assert.assertEquals(mutableListOf("2.4", "+", "7", "-", "2", "*", "5"), expressionSplitField.get(sut))

        removeResolvedOperationMethod.invoke(sut, 5, 10)
        Assert.assertEquals(mutableListOf("2.4", "+", "7", "-", "10.0"), expressionSplitField.get(sut))
    }

    @Test
    fun `test getNewExpression method should return correct new expression`() {
        val expressionSplitField = sut.javaClass.getDeclaredField("expressionSplit")
        expressionSplitField.isAccessible = true
        expressionSplitField.set(sut, mutableListOf("12", "/", "5", "+", "7", "-", "2", "*", "5"))

        val getNewExpressionMethod = sut.javaClass.getDeclaredMethod("getNewExpression")
        getNewExpressionMethod.isAccessible = true

        expressionSplitField.set(sut, mutableListOf("2.4", "+", "7", "-", "2", "*", "5"))
        Assert.assertEquals("2.4+7-2*5", getNewExpressionMethod.invoke(sut))

        expressionSplitField.set(sut, mutableListOf("2.4", "+", "7", "-", "10.0"))
        Assert.assertEquals("2.4+7-10.0", getNewExpressionMethod.invoke(sut))
    }

    @Test
    fun `test prepareExpressionToResolve method should return correct expression prepared`() {
        val prepareExpressionToResolveMethod = sut.javaClass.getDeclaredMethod("prepareExpressionToResolve", String::class.java)
        prepareExpressionToResolveMethod.isAccessible = true

        Assert.assertEquals("", prepareExpressionToResolveMethod.invoke(sut, ""))
        Assert.assertEquals("2", prepareExpressionToResolveMethod.invoke(sut, "2+"))
        Assert.assertEquals("", prepareExpressionToResolveMethod.invoke(sut, "+"))
        Assert.assertEquals("2*5", prepareExpressionToResolveMethod.invoke(sut, "2*5-"))
        Assert.assertEquals("2.3*5+7.2", prepareExpressionToResolveMethod.invoke(sut, "2,3*5+7,2"))
    }
}