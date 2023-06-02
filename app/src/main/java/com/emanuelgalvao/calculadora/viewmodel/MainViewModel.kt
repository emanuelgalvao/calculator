package com.emanuelgalvao.calculadora.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emanuelgalvao.calculadora.utils.*
import kotlin.math.absoluteValue

class MainViewModel: ViewModel() {

    private val _expression: MutableLiveData<String> = MutableLiveData("")
    val expression: LiveData<String> = _expression

    private val _result: MutableLiveData<String> = MutableLiveData("")
    val result: LiveData<String> = _result

    private val expressionHandler = ExpressionHandler()

    fun addToExpression(value: String) {
        val newExpression = validateAndAddValueToExpression(value)
        _expression.value = newExpression
        resolveExpression()
    }

    fun clearExpressionAndResult() {
        clearExpression()
        _result.value = ""
    }

    fun deleteLastInput() {
        _expression.value = _expression.value.toString().removeLastChar()
        resolveExpression()
    }

    fun clearExpression() {
        _expression.value = ""
    }

    fun invertExpressionSignal() {
        if (_expression.value.toString().isEmpty()) {
            _expression.value = CalculatorOperations.MINUS.symbol + _expression.value
        } else  if (_expression.value.toString().first().toString() == CalculatorOperations.MINUS.symbol) {
            val stringBuilder = StringBuilder(_expression.value.toString())
            _expression.value = stringBuilder.deleteAt(0).toString()
        } else {
            _expression.value = CalculatorOperations.MINUS.symbol + _expression.value
        }
        resolveExpression()
    }

    private fun resolveExpression() {
        _expression.value?.let {
            try {
                val expressionResult = expressionHandler.resolve(it)
                _result.value = formatResult(expressionResult)
            } catch (_: ExpressionException) {
                _result.value = ""
            }
        }
    }

    private fun formatResult(expressionResult: Double): String {
        return if (expressionResult.rem(1).absoluteValue.equals(0.0)) {
            expressionResult.toInt().toString()
        } else {
            expressionResult.toString().replace(Constants.DOT_SYMBOL, Constants.COMMA_SYMBOL)
        }
    }

    private fun validateAndAddValueToExpression(value: String): String {
        if (_expression.value.toString().isNotEmpty() &&
            verifyIfLastInputIsSymbol() &&
            value.isCalculatorSymbol()) {
            deleteLastInput()
        }

        if (_expression.value.toString().isEmpty() &&
            _result.value.toString().isNotEmpty() &&
            value.isCalculatorSymbol()) {
            return _result.value + value
        } else if (_expression.value.toString().isEmpty() &&
            value.isCalculatorSymbol() &&
            value != CalculatorOperations.MINUS.symbol) {
            return _expression.value.toString()
        }
        return _expression.value + value
    }

    private fun verifyIfLastInputIsSymbol(): Boolean {
        val expression = _expression.value.toString()
        return if (expression.isNotEmpty()) {
            expression[expression.lastIndex].toString().isCalculatorSymbol()
        } else {
            false
        }
    }
}