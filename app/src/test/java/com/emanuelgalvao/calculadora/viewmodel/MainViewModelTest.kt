package com.emanuelgalvao.calculadora.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class MainViewModelTest {

    private lateinit var sut: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        sut = MainViewModel()
    }

    @Test
    fun `test addToExpression method should add correctly the value to the expression`() {
        sut.addToExpression("*")
        Assert.assertEquals("", sut.expression.value)
        Assert.assertEquals("", sut.result.value)

        sut.addToExpression("-")
        Assert.assertEquals("-", sut.expression.value)
        Assert.assertEquals("", sut.result.value)

        sut.addToExpression("2")
        Assert.assertEquals("-2", sut.expression.value)
        Assert.assertEquals("-2", sut.result.value)

        sut.addToExpression("+")
        Assert.assertEquals("-2+", sut.expression.value)
        Assert.assertEquals("-2", sut.result.value)

        sut.addToExpression("-")
        Assert.assertEquals("-2-", sut.expression.value)
        Assert.assertEquals("-2", sut.result.value)

        sut.addToExpression("6")
        Assert.assertEquals("-2-6", sut.expression.value)
        Assert.assertEquals("-8", sut.result.value)

        sut.addToExpression("*")
        Assert.assertEquals("-2-6*", sut.expression.value)
        Assert.assertEquals("-8", sut.result.value)

        sut.addToExpression("/")
        Assert.assertEquals("-2-6/", sut.expression.value)
        Assert.assertEquals("-8", sut.result.value)

        sut.addToExpression("4")
        Assert.assertEquals("-2-6/4", sut.expression.value)
        Assert.assertEquals("-3,5", sut.result.value)

        sut.addToExpression("*")
        Assert.assertEquals("-2-6/4*", sut.expression.value)
        Assert.assertEquals("-3,5", sut.result.value)

        sut.addToExpression("2")
        Assert.assertEquals("-2-6/4*2", sut.expression.value)
        Assert.assertEquals("-5", sut.result.value)
    }

    @Test
    fun `test clearExpressionAndResult method should clear expression LiveData and result LiveData`() {
        sut.addToExpression("2")
        sut.addToExpression("+")
        sut.addToExpression("3")

        Assert.assertEquals("2+3", sut.expression.value)
        Assert.assertEquals("5", sut.result.value)

        sut.clearExpressionAndResult()

        Assert.assertEquals("", sut.expression.value)
        Assert.assertEquals("", sut.result.value)
    }

    @Test
    fun `test deleteLastInput method should delete correct the last input and update expression and update result`() {
        sut.addToExpression("7")
        sut.addToExpression("*")
        sut.addToExpression("3")
        sut.addToExpression("5")

        Assert.assertEquals("7*35", sut.expression.value)
        Assert.assertEquals("245", sut.result.value)

        sut.deleteLastInput()

        Assert.assertEquals("7*3", sut.expression.value)
        Assert.assertEquals("21", sut.result.value)
    }

    @Test
    fun `test clearExpression method should clear expression and keep result`() {
        sut.addToExpression("12")
        sut.addToExpression("/")
        sut.addToExpression("4")

        Assert.assertEquals("12/4", sut.expression.value)
        Assert.assertEquals("3", sut.result.value)

        sut.clearExpression()

        Assert.assertEquals("", sut.expression.value)
        Assert.assertEquals("3", sut.result.value)
    }

    @Test
    fun `test invertExpressionSignal method should update result and clear expression`() {

        Assert.assertEquals("", sut.expression.value)
        Assert.assertEquals("", sut.result.value)

        sut.invertExpressionSignal()

        Assert.assertEquals("-", sut.expression.value)
        Assert.assertEquals("", sut.result.value)

        sut.addToExpression("12")
        sut.addToExpression("-")
        sut.addToExpression("10")

        Assert.assertEquals("-12-10", sut.expression.value)
        Assert.assertEquals("-22", sut.result.value)

        sut.invertExpressionSignal()

        Assert.assertEquals("12-10", sut.expression.value)
        Assert.assertEquals("2", sut.result.value)

        sut.invertExpressionSignal()

        Assert.assertEquals("-12-10", sut.expression.value)
        Assert.assertEquals("-22", sut.result.value)
    }

    @Test
    fun `test resolveExpression method when expression is empty`() {
        val resolveExpressionMethod = sut.javaClass.getDeclaredMethod("resolveExpression")
        resolveExpressionMethod.isAccessible = true

        val expressionField = sut.javaClass.getDeclaredField("_expression")
        expressionField.isAccessible = true

        val resultField = sut.javaClass.getDeclaredField("_result")
        resultField.isAccessible = true

        expressionField.set(sut, MutableLiveData(""))
        Assert.assertEquals("", (resultField.get(sut) as MutableLiveData<*>).value)
    }

    @Test
    fun `test resolveExpression method when expression is valid`() {
        val resolveExpressionMethod = sut.javaClass.getDeclaredMethod("resolveExpression")
        resolveExpressionMethod.isAccessible = true

        val expressionField = sut.javaClass.getDeclaredField("_expression")
        expressionField.isAccessible = true

        val resultField = sut.javaClass.getDeclaredField("_result")
        resultField.isAccessible = true

        expressionField.set(sut, MutableLiveData("2*4"))
        resolveExpressionMethod.invoke(sut)
        Assert.assertEquals("8", (resultField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData("2*4.5"))
        resolveExpressionMethod.invoke(sut)
        Assert.assertEquals("9", (resultField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData("7-3*2"))
        resolveExpressionMethod.invoke(sut)
        Assert.assertEquals("1", (resultField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData("7-3*2+"))
        resolveExpressionMethod.invoke(sut)
        Assert.assertEquals("1", (resultField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData("-7-3*2+"))
        resolveExpressionMethod.invoke(sut)
        Assert.assertEquals("-13", (resultField.get(sut) as MutableLiveData<*>).value)
    }

    @Test
    fun `test formatResult method should return correct format to different scenarios`() {
        val formatResultMethod = sut.javaClass.getDeclaredMethod("formatResult", Double::class.java)
        formatResultMethod.isAccessible = true

        Assert.assertEquals("1", formatResultMethod.invoke(sut, 1.0))
        Assert.assertEquals("-75", formatResultMethod.invoke(sut, -75.0))
        Assert.assertEquals("3,5", formatResultMethod.invoke(sut, 3.5))
        Assert.assertEquals("-14,6", formatResultMethod.invoke(sut, -14.6))
    }

    @Test
    fun `test validateAndAddValueToExpression method when expression is empty and user want to add a symbol`() {
        val validateAndAddValueToExpressionMethod = sut.javaClass.getDeclaredMethod("validateAndAddValueToExpression", String::class.java)
        validateAndAddValueToExpressionMethod.isAccessible = true

        val expressionField = sut.javaClass.getDeclaredField("_expression")
        expressionField.isAccessible = true

        expressionField.set(sut, MutableLiveData(""))
        Assert.assertEquals("", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "*")))
        Assert.assertEquals("", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "/")))
        Assert.assertEquals("", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "+")))
        Assert.assertEquals("", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, ",")))
        Assert.assertEquals("", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "-")))
        Assert.assertEquals("-", (expressionField.get(sut) as MutableLiveData<*>).value)
    }

    @Test
    fun `test validateAndAddValueToExpression method when expression is empty, but has a previous result and user want to add a symbol`() {
        val validateAndAddValueToExpressionMethod =
            sut.javaClass.getDeclaredMethod("validateAndAddValueToExpression", String::class.java)
        validateAndAddValueToExpressionMethod.isAccessible = true

        val expressionField = sut.javaClass.getDeclaredField("_expression")
        expressionField.isAccessible = true

        val resultField = sut.javaClass.getDeclaredField("_result")
        resultField.isAccessible = true

        resultField.set(sut, MutableLiveData("20.4"))
        Assert.assertEquals("20.4", (resultField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "*")))
        Assert.assertEquals("20.4*", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "2")))
        Assert.assertEquals("20.4*2", (expressionField.get(sut) as MutableLiveData<*>).value)
    }

    @Test
    fun `test validateAndAddValueToExpression method when expression is not empty, but the previous input is symbol and user want to add a symbol`() {
        val validateAndAddValueToExpressionMethod =
            sut.javaClass.getDeclaredMethod("validateAndAddValueToExpression", String::class.java)
        validateAndAddValueToExpressionMethod.isAccessible = true

        val expressionField = sut.javaClass.getDeclaredField("_expression")
        expressionField.isAccessible = true

        expressionField.set(sut, MutableLiveData("30.5-"))
        Assert.assertEquals("30.5-", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "*")))
        Assert.assertEquals("30.5*", (expressionField.get(sut) as MutableLiveData<*>).value)

        expressionField.set(sut, MutableLiveData(validateAndAddValueToExpressionMethod.invoke(sut, "/")))
        Assert.assertEquals("30.5/", (expressionField.get(sut) as MutableLiveData<*>).value)
    }

    @Test
    fun `test verifyIfLastInputIsSymbol method should return correct to different scenarios`() {
        val verifyIfLastInputIsSymbolMethod = sut.javaClass.getDeclaredMethod("verifyIfLastInputIsSymbol")
        verifyIfLastInputIsSymbolMethod.isAccessible = true

        Assert.assertEquals(false, verifyIfLastInputIsSymbolMethod.invoke(sut))
        sut.addToExpression("-")
        Assert.assertEquals(true, verifyIfLastInputIsSymbolMethod.invoke(sut))
        sut.addToExpression("2")
        Assert.assertEquals(false, verifyIfLastInputIsSymbolMethod.invoke(sut))
        sut.addToExpression("*")
        Assert.assertEquals(true, verifyIfLastInputIsSymbolMethod.invoke(sut))
        sut.addToExpression("4")
        Assert.assertEquals(false, verifyIfLastInputIsSymbolMethod.invoke(sut))
    }


}