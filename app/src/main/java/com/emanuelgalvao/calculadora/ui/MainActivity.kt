package com.emanuelgalvao.calculadora.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.emanuelgalvao.calculadora.viewmodel.MainViewModel
import com.emanuelgalvao.calculadora.databinding.ActivityMainBinding
import com.emanuelgalvao.calculadora.utils.CalculatorOperations
import com.emanuelgalvao.calculadora.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)

        setObservers()
        setListeners()
    }

    private fun setObservers() {
        viewModel.expression.observe(this) { expression ->
            binding.tvExpression.text = expression
        }
        viewModel.result.observe(this) { result ->
            binding.tvResult.text = result
            binding.tvEqual.visibility = if (result.isEmpty()) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun setListeners() {
        val numberButtons = getNumberButtons()
        numberButtons.forEach { button ->
            button.setOnClickListener {
                clickValueButton(button.text.toString())
            }
        }

        binding.btDivision.setOnClickListener { clickValueButton(CalculatorOperations.DIVISION.symbol) }
        binding.btTimes.setOnClickListener { clickValueButton(CalculatorOperations.MULTIPLY.symbol) }
        binding.btPlus.setOnClickListener { clickValueButton(CalculatorOperations.SUM.symbol) }
        binding.btMinus.setOnClickListener { clickValueButton(CalculatorOperations.MINUS.symbol) }
        binding.btPoint.setOnClickListener { clickValueButton(Constants.COMMA_SYMBOL) }

        binding.btClear.setOnClickListener { clickClearButton() }
        binding.btDelete.setOnClickListener { clickDeleteButton() }
        binding.btPositiveNegative.setOnClickListener { clickInvertSignalButton() }
        binding.btEqual.setOnClickListener { clickEqualButton() }
    }

    private fun clickValueButton(value: String) {
        viewModel.addToExpression(value)
    }

    private fun clickClearButton() {
        viewModel.clearExpressionAndResult()
    }

    private fun clickDeleteButton() {
        viewModel.deleteLastInput()
    }

    private fun clickInvertSignalButton() {
        viewModel.invertExpressionSignal()
    }

    private fun clickEqualButton() {
        viewModel.clearExpression()
    }

    private fun getNumberButtons() = arrayListOf(
        binding.btZero,
        binding.btOne,
        binding.btTwo,
        binding.btThree,
        binding.btFour,
        binding.btFive,
        binding.btSix,
        binding.btSeven,
        binding.btEight,
        binding.btNine)
}