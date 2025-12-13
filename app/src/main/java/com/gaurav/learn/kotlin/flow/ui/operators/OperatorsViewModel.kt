package com.gaurav.learn.kotlin.flow.ui.operators

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.flow.data.api.ApiHelper
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.flow.utils.DispatcherProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class OperatorsViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val TAG = "~~~~~OperatorsViewModel"

    private val _uiState = MutableStateFlow<OperatorsUiState<Any>>(OperatorsUiState.Loading)

    val uiState: StateFlow<OperatorsUiState<Any>> = _uiState


    /**
     * map is used to return a flow containing the results of applying the
     * given transform function to each value of the original flow.
     */
    fun useMapOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = OperatorsUiState.Loading

            // API Call
            getNumbersFromAPI(1000)
                .flowOn(dispatcherProvider.io)
                .map { num ->
                    // Multiply each number by 2
                    num * 2
                }
                .flowOn(dispatcherProvider.default)
                .catch { e ->
                    _uiState.value = OperatorsUiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = OperatorsUiState.Success(it)
                }

        }
    }

    /**
     * filter is used to return a flow containing only values of the original flow that match the given condition.
     */
    fun useFilterOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = OperatorsUiState.Loading

            // API Call
            getNumbersFromAPI(1000)
                .flowOn(dispatcherProvider.io)
                .filter { num ->
                    // Filter numbers greater than 3
                    num > 3
                }
                .flowOn(dispatcherProvider.default)
                .catch { e ->
                    _uiState.value = OperatorsUiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = OperatorsUiState.Success(it)
                }

        }
    }

    fun useFilterIsInstanceOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = OperatorsUiState.Loading

            // API Call
            getNumbersFromAPI(1000)
                .flowOn(dispatcherProvider.io)
                .map { num ->
                    // Returning different types based on condition
                    if (num % 2 == 0) {
                        num.toString() // Even numbers as String
                    } else {
                        num // Odd numbers as Int
                    }
                }
                .filterIsInstance<Int>() // Filter only Int values
                .flowOn(dispatcherProvider.default)
                .catch { e ->
                    _uiState.value = OperatorsUiState.Error(e.toString())
                }
                .collect {
                    Log.e(TAG, "Filter Is Instance Flow Item: ==> : $it , Type: ${it::class.java.simpleName}")
                    _uiState.value = OperatorsUiState.Success(it)
                }
        }
    }

    fun useWithIndexOperator() {
        viewModelScope.launch(dispatcherProvider.main) {
            val stringFlow: Flow<String> = flowOf("a", "b", "c")
            stringFlow
                .withIndex()
                .collect {
                    it.value // The actual value
                    it.index // The index of the value
                    Log.e(TAG, "WithIndex Flow Item: ==> : $it , Type: ${it::class.java.simpleName}")
                }
        }
    }

    fun useOnEachOperator() {
        viewModelScope.launch(dispatcherProvider.main) {
            val stringFlow: Flow<String> = flowOf("a", "b", "c")
            stringFlow
                .onEach {
                    doSomething(it)
                }
                .collect {
                    Log.e(TAG, "OnEach Flow Item: ==> : $it , Type: ${it::class.java.simpleName}")
                }
        }
    }


    /*
    Time:   0   5   10   15   20   25   30   35   40   45   50
    flow1:  1---|----|----2----3----|----4----|----5----|
    flow2:  |---A----B----|----|----C----|----D----|----E----F---|
    merge:  1---A----B----2----3----C----4----D----5----E----F---|
     */
    fun useMergeOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            val flow =  createSampleFlows()
            val flow1 = flow.first
            val flow2 = flow.second

            Log.w(TAG, "Merge will execute 2 parallel concurrent flows [one for outer and one for inner flow] \n and emit values as soon as they are emitted from any of the flow.")
            merge(flow1, flow2)
                .collect { value ->
                    Log.e(TAG, "Merge Flow Item: ==> : $value , Type: ${value::class.java.simpleName}")
                }

        }
    }


    fun useFlatMapMergeOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            val flow1 = flow {
                printJobInfo("flow_1")
                emit(1)
                delay(15)
                emit(2)
                delay(10)
                emit(3)
                delay(5)
                emit(4)
            }
            val flow2 = flow {
                printJobInfo("flow_2")
                emit("A")
                delay(12)
                emit("B")
            }

            Log.w(TAG, "Flat Map Merge will execute inner flows concurrently in parallel \n [for each outer flow emit value, there will be one separate concurrent inner flow] \n and emit values as soon as they are emitted from any of the inner flow.")
            flow1
                .flatMapMerge { number ->
                    Log.e(TAG, "Flat Map Merge Flow Item: ==> : $number ")
                    flow2.withIndex()
                }
                .collect { value ->
                    Log.e(TAG, "Flat Map Merge Collected Flow Item: ==> : $value")
                }

        }
    }

    fun useFlatMapLatestOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            val flow1 = flow {
                printJobInfo("flow_1")
                emit(1)
                delay(15)
                emit(2)
                delay(10)
                emit(3)
                delay(5)
                emit(4)
            }
            val flow2 = flow {
                printJobInfo("flow_2")
                emit("A")
                delay(12)
                emit("B")
            }

            Log.w(TAG, "Flat Map Latest will cancel the previous inner flow \n and start new inner flow for each outer flow emit value.")
            flow1
                .flatMapLatest { number ->
                    Log.e(TAG, "Flat Map Latest Flow Item: ==> : $number ")
                    flow2.withIndex()
                }
                .collect { value ->
                    Log.e(TAG, "Flat Map Latest Collected Flow Item: ==> : $value")
                }

        }
    }

    /*****************************************************************/
    /****************** zip ********************************/

    fun useZipOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            val flow1 = flowOf(1, 2, 3).onEach {
                if(it == 1)
                    printJobInfo("flow_1")
                delay(10)
            }
            val flow2 = flowOf("a", "b", "c", "d").onEach {
                if(it == "a")
                    printJobInfo("flow_2")
                delay(15)
            }

            Log.w(TAG, "Zip will wait for both flows to emit value \n and then combine those values using the provided transform function.")
            flow1.zip(flow2) { a, b -> "{$a,$b}" }
                .collect {
                    Log.e(TAG, "Zip Flow Item: ==> : $it , Type: ${it::class.java.simpleName}")
                }

            //delay(2000)
            Log.e(TAG, "Now Call useZipOperatorToExecuteTowTaskInParallel for example 2 API Calls in parallel")
            zipToSimulateMultipleNetworkCallInParallel()

        }
    }

    /**
     * zip is used to execute the task in parallel and combine the results.
     * For example, execute multiple network calls in parallel.
     */
    fun zipToSimulateMultipleNetworkCallInParallel() {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = OperatorsUiState.Loading

            // API Call
            getNumbersFromAPI(1000)
                .zip(getAlphabetsFromAPI(2000)) { number, char ->
                    //"$number -> $char"
                    val allData = mutableListOf<Any>()
                    allData.add(number)
                    allData.add(char)
                    return@zip allData
                }
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = OperatorsUiState.Error(e.toString())
                }
                .collect {
                    // Here the type will be mutableList as we have combined Int and Char to form mutableList
                    // in the zip operator above.
                    Log.e(TAG, "Zipped Flow Item: ==> : $it , Type: ${it::class.java.simpleName}")
                    _uiState.value = OperatorsUiState.Success(it)
                }

        }
    }

    /****************** flattenConcatExample ********************************/

    /**
     *
     * flattenConcat is also used to execute the task in series,
     * i.e. first getNumbersFromAPI() will execute completely after that only
     * getAlphabetsFromAPI() will start executing.
     *
     * The flattenConcat method  is used to concatenate multiple flows emitted by an upstream flow,
     * collecting and emitting their values sequentially.
     * It waits for each inner flow to complete before moving to the next, preserving the order.
     *
     */
    fun useFlattenConcatOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = OperatorsUiState.Loading

            flowOf(getNumbersFromAPI(1000), getAlphabetsFromAPI(2000))
                .flattenConcat()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = OperatorsUiState.Error(e.toString())
                }
                .collect {
                    Log.e(TAG, "Flatten Concat Flow Item: ==> : $it , Type: ${it::class.java.simpleName}")
                    _uiState.value = OperatorsUiState.Success(it)
                }

        }
    }

    /****************** flatMapConcatExample ********************************/

    /**
     * For example, execute multiple network calls in series.
     */
    fun useFlatMapConcatOperatorToExecuteTaskInSeries() {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = OperatorsUiState.Loading
            val allData = mutableListOf<Any>()

            getNumbersFromAPI(10)
                .flatMapConcat { number ->
                    allData.add(number)
                    getAlphabetsFromAPI(2000)
                }
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = OperatorsUiState.Error(e.toString())
                }
                .collect {
                    allData.add(it)
                    Log.e(TAG, "FlatMap Concat Flow Item: ==> : $it , Type: ${it::class.java.simpleName} , allData: $allData")
                    // StateFlow does not emit same object again and again even when the value inside that object is changing
                    // So we need to create new object again and again so that it should emit on UI layer
                    val newList = allData.toList()
                    //_uiState.value = OperatorsUiState.Success(allData)
                    _uiState.value = OperatorsUiState.Success(newList)
                }

        }
    }

    /****************** flatMapConcat_Vs_flattenConcat ********************************/

    fun useFlattenConcatVsFlatMapConcatOperator() {
        viewModelScope.launch(dispatcherProvider.main) {

            val flow1 = flowOf(1, 2, 3).onEach {
                printJobInfo("flow_1")
                delay(10)
            }
            val flow2 = flowOf("a", "b", "c", "d").onEach {
                printJobInfo("flow_2")
                delay(15)
            }

            /*
                Source:   -- Flow1 -- Flow2 -- Flow3 --|
                |
                v
                flattenConcat(): -- Flow1 values -- Flow2 values -- Flow3 values --|
            */

            Log.e(TAG, "flattenConcatFlow==> Starting")

            flowOf(flow1, flow2)
                .flattenConcat()
                .collect { value ->
                    Log.e(TAG, "Flatten Concat Flow Item: ==> : $value , Type: ${value::class.java.simpleName}")
                }

            Log.e(TAG, "flatMapConcatFlow==> Starting")
            delay(3000)

            flow1
                .flatMapConcat { number ->
                    Log.e(TAG, "1. Flat Map Concat Flow Item: ==> : $number , Type: ${number::class.java.simpleName}")
                    flow2
                }
                .collect { value ->
                    Log.e(TAG, "2. Flat Map Concat Flow Item: ==> : $value , Type: ${value::class.java.simpleName}")
                }
        }
    }


    /*****************************************************************/

    /*************************** Helper Methods ***************************/

    private fun getNumbersFromAPI(delayTime: Long): Flow<Int> {
        // Simulate API Call
        return flow {
            printJobInfo("flow_1")
            delay(1000)
            val list = listOf(1, 2, 3, 4, 5, 6)
            Log.w(TAG, "getNumbersFromAPI() function call...")
            for (item in list) {
                emit(item)
                delay(delayTime)
            }
        }
    }

    private fun getAlphabetsFromAPI(delayTime: Long = 2000): Flow<Char> {
        // Simulate API Call
        return flow {
            printJobInfo("flow_2")
            delay(1000)
            val list = ('A'..'H').toList()
            Log.w(TAG, "getAlphabetsFromAPI() function call...")
            for (item in list) {
                emit(item)
                delay(delayTime)
            }
        }
    }

    private suspend fun doSomething(value: String) {
        // Side effects (like logging) can occur for each item.
        Log.w(TAG, "Logs doSomething before collect.....  and the Value is: $value")
        // simulate some work
        delay(2000)
    }

    private fun simulateError(): Flow<Int> {
        return flow {
            emit(1)
            emit(2)
            throw Exception("Simulated Error in Flow")
        }
    }

    private suspend fun printJobInfo(name: String) {
        // Access the current coroutine context
        val context = currentCoroutineContext()
        // Get the Job instance from the context
        val job = context[Job]

        Log.e(TAG, "$name , Job ID : $job")
    }

    private fun createSampleFlows(): Pair<Flow<Int>, Flow<String>> {

        val flow1 = flow {
            printJobInfo("flow_1")
            emit(1)
            delay(15)
            emit(2)
            delay(5)
            emit(3)
            delay(10)
            emit(4)
            delay(10)
            emit(5)
        }

        val flow2 = flow {
            printJobInfo("flow_2")
            delay(5)
            emit("A")
            delay(5)
            emit("B")
            delay(15)
            emit("C")
            delay(10)
            emit("D")
            delay(10)
            emit("E")
            delay(5)
            emit("F")
        }

        return Pair(flow1, flow2)
    }


}

sealed interface OperatorsUiState<out T> {

    data class Success<T>(val data: T) : OperatorsUiState<T>

    data class Error(val message: String) : OperatorsUiState<Nothing>

    object Loading : OperatorsUiState<Nothing>

}