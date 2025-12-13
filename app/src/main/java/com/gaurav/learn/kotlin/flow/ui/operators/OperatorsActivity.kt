package com.gaurav.learn.kotlin.flow.ui.operators

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gaurav.learn.kotlin.flow.R
import com.gaurav.learn.kotlin.flow.data.api.ApiHelperImpl
import com.gaurav.learn.kotlin.flow.data.api.RetrofitBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelperImpl
import com.gaurav.learn.kotlin.flow.ui.base.ViewModelFactory
import com.gaurav.learn.kotlin.flow.utils.DefaultDispatcherProvider
import kotlinx.coroutines.launch


class OperatorsActivity : AppCompatActivity() {

    private val TAG = "~~~~~OperatorsActivity"

    private lateinit var viewModel: OperatorsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operators)
        setupViewModel()
        setupObserver()
    }

    /****************** TRANSFORM OPERATORS **************************/

    fun useOfFilterOperator(view: View) {
        viewModel.useFilterOperator()
    }


    fun useOfFilterIsInstanceOperator(view: View) {
        viewModel.useFilterIsInstanceOperator()
    }

    fun useOfMapOperator(view: View) {
        viewModel.useMapOperator()
    }

    fun useOfWithIndexOperator(view: View) {
        viewModel.useWithIndexOperator()
    }

    fun useOfOnEachOperator(view: View) {
        viewModel.useOnEachOperator()
    }

    /****************** ZIP OPERATORS **************************/

    fun useOfZipOperator(view: View) {
        //viewModel.useZipOperatorToExecuteTowTaskInParallel()
        viewModel.useZipOperator()
    }

    /****************** MERGE OPERATORS **************************/

    fun useOfMergeOperator(view: View) {
        viewModel.useMergeOperator()
    }

    fun useOfFlatMapConcatOperator(view: View) {
        viewModel.useFlatMapConcatOperatorToExecuteTaskInSeries()
    }

    fun useOfFlattenConcatOperator(view: View) {
        viewModel.useFlattenConcatOperator()
    }

    fun useOfFlattenConcatVsFlatMapConcat(view: View) {
        viewModel.useFlattenConcatVsFlatMapConcatOperator()
    }

    fun useOfFlatMapMergeOperator(view: View) {
        viewModel.useFlatMapMergeOperator()
    }

    fun useOfFlatMapLatestOperator(view: View) {
        viewModel.useFlatMapLatestOperator()
    }

    fun useOfZipOperatorToSimulateParallelNetworkCalls(view: View) {
        viewModel.zipToSimulateMultipleNetworkCallInParallel()
    }


    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is OperatorsUiState.Success -> {
                            Log.d(TAG, "Success Status ==> : ${it.data}")
                        }

                        is OperatorsUiState.Loading -> {
                            Log.d(TAG, "Loading Status ==> : $it")
                        }

                        is OperatorsUiState.Error -> {
                            Log.d(TAG, "Error Status ==> : ${it.message}")
                        }

                    }
                }
            }
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                DefaultDispatcherProvider()
            )
        )[OperatorsViewModel::class.java]
    }

}