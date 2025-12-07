package com.gaurav.learn.kotlin.flow.ui.coldFlow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.flow.data.api.ApiHelper
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.flow.utils.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ColdFlowViewModel(
    val apiHelper: ApiHelper, dbHelper: DatabaseHelper, val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "ColdFlowViewModel"
    }


    fun startColdFlowTask1() {

        viewModelScope.launch(dispatcherProvider.main) {

            // below is the flow chain
            doLongRunningTask1()
                .collect { value ->
                    Log.d(TAG, "1. Cold Flow [ flow{} ] emitted value: $value on thread: ${Thread.currentThread().name}"
                    )
                }

            delay(100)

            doLongRunningTask1()
                .collect { value ->
                    Log.d(TAG, "2. Cold Flow [ flow{} ] emitted value: $value on thread: ${Thread.currentThread().name}"
                    )
                }

            delay(100)

            doLongRunningTask1()

        }
    }

    fun startColdFlowTask2() {

        viewModelScope.launch(dispatcherProvider.main) {

            // below is the flow chain
            doLongRunningTask2()
                .collect { value ->
                    Log.d(TAG, "1. Cold Flow [ flowOf() ] emitted value: $value on thread: ${Thread.currentThread().name}"
                    )
                }

            delay(100)

            doLongRunningTask2()
                .collect { value ->
                    Log.d(TAG, "2. Cold Flow [ flowOf() ] emitted value: $value on thread: ${Thread.currentThread().name}"
                    )
                }

            delay(100)

            doLongRunningTask2()

        }
    }

    fun startColdFlowTask3() {

        viewModelScope.launch(dispatcherProvider.main) {

            // below is the flow chain
            doLongRunningTask3()
                .collect { value ->
                    Log.d(TAG, "1. Cold Flow [ asFlow() ] emitted value: $value on thread: ${Thread.currentThread().name}"
                    )
                }

            delay(100)

            doLongRunningTask3()
                .collect { value ->
                    Log.d(TAG, "2. Cold Flow [ asFlow() ] emitted value: $value on thread: ${Thread.currentThread().name}"
                    )
                }

            delay(100)

            doLongRunningTask3()

        }
    }

    /**
     * Cold Flow:- By default, flows are cold.
     * Characteristics of Cold Flow:-
     * 1. It emits data only when there is a collector/subscribers.
     * 2. It does not store data.
     * 3. It can't have multiple collectors. Hence Each collector gets its own separate flow.
     *    Each collector triggers a new execution of the flow, leading to potentially different results for each collection.
     * 4. Each time a terminal operator (like collect) is called, the flow is re-executed from the beginning.
     *
     * 5. Real Life Example is YouTube videos on-demand (i.e., when you click to play a video).
     * 6. Example builders: flow {}, flowOf(), asFlow().
     *
     * 7. Use cases: Network calls, database queries, user input events, etc.
     * 8. Example: A flow that fetches user details from a network API each time it is collected.
     * 9. Suitable for scenarios where data needs to be fetched or computed on-demand.
     *
     * 10. By default, flows are cold.
     * 11. It can be converted to hot flow using operators like shareIn and stateIn.
     * 12. In this example, the doLongRunningTask() function returns a cold flow that simulates a long-running task.
     *
     */
    private fun doLongRunningTask1(): Flow<Int> {

        // 1. Flow builder - flow{}
        // Internal:- fun <T> flow(@BuilderInference block: suspend FlowCollector<T>.() -> Unit): Flow<T> = SafeFlow(block)
        val coldFlow = flow {
            // your code for doing a long running task
            // Added delay to simulate
            delay(2000)
            flowStartedLog("flow{}")
            for (i in 1..5) {
                delay(1000)
                emit(i)
            }
        }

        return coldFlow

    }


    private fun doLongRunningTask2(): Flow<Int> {

        // 2. Flow builder - flowOf()
        // Internal:- fun <T> flowOf(vararg elements: T): Flow<T>
        val coldFlow = flowOf(1, 2, 3, 4, 5)
            .map {
                if (it == 1)
                    flowStartedLog("flowOf()")
                it * 2
            }
            .onEach {
                delay(1000)
            }

        return coldFlow

    }

    private fun doLongRunningTask3(): Flow<Int> {

        // 3. Flow builder - asFlow()
        // Internal:- fun <T> Iterable<T>.asFlow(): Flow<T>
        val list = listOf(10, 20, 30, 40, 50)
        val coldFlow = list.asFlow()
            .map {
                if (it == 10)
                    flowStartedLog("asFlow()")
                it * 2
            }
            .onEach {
                delay(1000)
            }

        return coldFlow

    }

    private fun flowStartedLog(type: String) {
        Log.e(TAG, "My cold flow [ $type ] is started.")
    }

}