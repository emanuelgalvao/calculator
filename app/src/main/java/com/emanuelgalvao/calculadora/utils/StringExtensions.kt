package com.emanuelgalvao.calculadora.utils

fun String.removeLastChar(): String {
    val stringBuilder = StringBuilder(this)
    if (stringBuilder.isNotEmpty()) {
        return stringBuilder.deleteAt(stringBuilder.lastIndex).toString()
    }
    return this
}

fun String.isCalculatorSymbol(): Boolean {

    return this == CalculatorOperations.SUM.symbol ||
            this == CalculatorOperations.MINUS.symbol ||
            this == CalculatorOperations.MULTIPLY.symbol ||
            this == CalculatorOperations.DIVISION.symbol ||
            this == Constants.COMMA_SYMBOL
}