package com.gaurav.learn.kotlin.flow.ui.hotFlow.sharedFlow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.flow.data.api.ApiHelper
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.flow.utils.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SharedFlowViewModel(
    val apiHelper: ApiHelper, dbHelper: DatabaseHelper, val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "~~~~~SharedFlowViewModel"
    }

    // Does not need an initial value so does not emit any value by default.
    private val _events = MutableSharedFlow<String>()
    //private val _events = MutableSharedFlow<String>(replay = 1)
    val events: SharedFlow<String> = _events


    /**
     * Hot Flow:- StateFlow & SharedFlow both are Hot Flow.
     * Characteristics of Hot Flow:-
     * 1. It emits data (values) even when there is no collector/subscribers.
     *    Hence It keeps running and emitting even if no one is collecting.
     * 2. It can store data.
     * 3. It can have multiple collectors.
     *    [ Real Life Example is Live { LIVE is important here } Cricket match on HotStar or Disney+ ]
     *
     *
     * SharedFlow Specific Characteristics:- StateFlow & SharedFlow both are Hot Flow.
     * 1. SharedFlow does not need an initial value so it does not emit any value by default.
     * 2. SharedFlow does not hold the last emitted value by default.
     *    But SharedFlow Can be configured to emit many previous values using the replay operator.
     *    By default, SharedFlow does not replay any value to new collectors.
     *    If you set the replay parameter (e.g. MutableSharedFlow(replay = 1)), it will hold
     *    and emit the last n values to new collectors.
     *    Without replay, new collectors only receive future emissions, not the last known value.
     * 3. It does not have the value property.
     * 4. It emits all the values and does not care about the distinct from the previous item.
     *    It emits consecutive repeated values also.
     * 5. SharedFlow is NOT similar to LiveData.
     * 6. SharedFlow is more general-purpose and can be used for various event-based scenarios.
     *    Example:- Event bus, notifications, for broadcasting events, etc.
     * 7. SharedFlow Use Case: Ideal for one-time events like navigation, showing a Snackbar, or broadcasting updates.
     * 8. SharedFlow is suitable for representing events or actions that occur over time, such as user interactions, notifications, or messages.
     *
     */
    public fun executeTask() {
        viewModelScope.launch(dispatcherProvider.main) {
            _events.emit("SomeLongRunningTaskStarted")
            delay(100) // Demonstrate that it can emit consecutive repeated values.
            _events.emit("SomeLongRunningTaskStarted")

            doSomeLongRunningTask()

            _events.emit("LongRunningTaskCompleted")
        }
    }

    private suspend fun doSomeLongRunningTask() {
        delay(3000)
        for (i in 1..5) {
            val updatedValue = "Downloading File $i"
            Log.e(TAG, "Updated Value is :: $updatedValue")
            _events.emit(updatedValue)
            delay(2000)
        }
    }


    private fun flowStartedLog() {
        Log.e(TAG, "My Hot flow [ SharedFlow ] is started.")
    }

}