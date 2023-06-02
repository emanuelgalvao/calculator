package com.emanuelgalvao.calculadora.utils

open class ExpressionHandler {

    private val regex = Regex(Constants.REGEX_SPLIT_OPERATORS)

    private lateinit var expression: String
    private lateinit var expressionSplit: MutableList<String>

    @Throws(ExpressionException::class)
    fun resolve(expression: String): Double {
        this.expression = prepareExpressionToResolve(expression)
        this.expressionSplit = this.expression.split(regex).toMutableList()

        resolveNegativeSignalAtExpressionStart()

        return if (hasExpressionToResolve()) {
            val operatorPosition = getOperatorPosition()
            val result = resolveOperation(operatorPosition)
            removeResolvedOperation(operatorPosition, result)
            resolve(getNewExpression())
        } else {
            try {
                this.expression.toDouble()
            } catch (exception: Exception) {
                throw ExpressionException("Invalid Expression")
            }
        }
    }

    private fun prepareExpressionToResolve(expression: String): String {
        var expressionPrepared = expression.replace(Constants.COMMA_SYMBOL, Constants.DOT_SYMBOL)
        if (expressionPrepared.isNotEmpty()
            && expressionPrepared.last().toString().isCalculatorSymbol()) {
            expressionPrepared = expressionPrepared.removeLastChar()
        }
        return expressionPrepared
    }

    private fun resolveNegativeSignalAtExpressionStart() {
        if (hasNegativeSignalAtExpressionStart()) {
            expressionSplit[2] = CalculatorOperations.MINUS.symbol + expressionSplit[2]
            expressionSplit.removeAt(1)
            expressionSplit.removeAt(0)
        }
    }

    private fun hasNegativeSignalAtExpressionStart(): Boolean {
        return expressionSplit.first().isEmpty() &&
                expressionSplit.size > 1
                && expressionSplit[1] == CalculatorOperations.MINUS.symbol
    }

    private fun hasExpressionToResolve(): Boolean {
        return expressionSplit.size > 1
    }

    private fun getOperatorPosition(): Int {
        return if (expressionSplit.contains(CalculatorOperations.MULTIPLY.symbol) ||
            expressionSplit.contains(CalculatorOperations.DIVISION.symbol)) {
            expressionSplit.indexOfFirst {
                it == CalculatorOperations.MULTIPLY.symbol ||
                it == CalculatorOperations.DIVISION.symbol
            }
        } else {
            expressionSplit.indexOfFirst {
                it == CalculatorOperations.SUM.symbol ||
                it == CalculatorOperations.MINUS.symbol
            }
        }
    }

    private fun resolveOperation(operatorPosition: Int): Double {
        val num1 = expressionSplit[operatorPosition - 1]
        val num2 = expressionSplit[operatorPosition + 1]
        val symbol = expressionSplit[operatorPosition]

        return calculate(symbol, num1, num2)
    }

    private fun calculate(symbol: String, num1: String, num2: String): Double {
        val calculator = Calculator()
        return when (symbol) {
            CalculatorOperations.SUM.symbol -> calculator.sum(num1.toDouble(), num2.toDouble())
            CalculatorOperations.MINUS.symbol -> calculator.minus(num1.toDouble(), num2.toDouble())
            CalculatorOperations.MULTIPLY.symbol -> calculator.multiply(num1.toDouble(), num2.toDouble())
            CalculatorOperations.DIVISION.symbol -> calculator.divide(num1.toDouble(), num2.toDouble())
            else -> 0.0
        }
    }

    private fun removeResolvedOperation(operatorPosition: Int, result: Double) {
        expressionSplit[operatorPosition] = result.toString()
        expressionSplit.removeAt(operatorPosition + 1)
        expressionSplit.removeAt(operatorPosition - 1)
    }

    private fun getNewExpression(): String {
        var newExpression = String()
        expressionSplit.forEach {
            newExpression += it
        }
        return newExpression
    }
}