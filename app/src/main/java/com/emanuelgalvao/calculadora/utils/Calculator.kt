package com.emanuelgalvao.calculadora.utils

class Calculator {

    fun sum(num1: Number, num2: Number): Double {
        return num1.toDouble() + num2.toDouble()
    }

    fun minus(num1: Number, num2: Number): Double {
        return num1.toDouble() - num2.toDouble()
    }

    fun multiply(num1: Number, num2: Number): Double {
        return num1.toDouble() * num2.toDouble()
    }

    fun divide(num1: Number, num2: Number): Double {
        return num1.toDouble() / num2.toDouble()
    }
}