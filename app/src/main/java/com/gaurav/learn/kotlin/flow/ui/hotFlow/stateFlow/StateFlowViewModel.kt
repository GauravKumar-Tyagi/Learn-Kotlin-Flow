package com.gaurav.learn.kotlin.flow.ui.hotFlow.stateFlow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.flow.data.api.ApiHelper
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.flow.utils.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StateFlowViewModel(
    val apiHelper: ApiHelper, dbHelper: DatabaseHelper, val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "~~~~~StateFlowViewModel"
    }

    // Always needs an initial value and emits it as soon as the collector starts collecting.
    private val _uiState = MutableStateFlow<String>("Initial value")
    val uiState: StateFlow<String> = _uiState

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
     * StateFlow Specific Characteristics:- StateFlow & SharedFlow both are Hot Flow.
     * 1. StateFlow always has an initial value and emits it as soon as the collector starts collecting.
     * 2. StateFlow always holds the last emitted value and only emits the last known value.
     * 3. When a new collector/subscriber comes, it immediately receives the last emitted value.
     * 4. Designed for state management in UI.
     * 5. StateFlow is designed to represent a state that can change over time.
     *    Hence It is useful for scenarios where you want to maintain and observe a state that can be updated.
     *    Example:- UI state management in Android applications.
     * 6. The best place to use StateFlow in your Android application is in the ViewModel layer.
     *    Hence, StateFlow will be used mostly on ViewModel level to hold and manage UI related state.
     *
     * 7. It has the value property, we can check the current value.
     *    It keeps a history of one value that we can get directly without collecting.
     * 8. It does not emit consecutive repeated values.
     *    It emits the value only when it is distinct from the previous item.
     * 9. StateFlow is Similar to LiveData except for the Lifecycle awareness of the Android component.
     *    We should use repeatOnLifecycle scope with StateFlow to add the Lifecycle awareness to it, then
     *    it will become exactly like LiveData.
     *
     */
    public fun executeTask() {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = "SomeLongRunningTaskStarted"
            delay(100) // Demonstrate that it does not emit consecutive repeated values.
            _uiState.value = "SomeLongRunningTaskStarted"

            doSomeLongRunningTask()

            _uiState.value = "LongRunningTaskCompleted"
        }
    }

    private suspend fun doSomeLongRunningTask() {
        delay(3000)
        for (i in 1..5) {
            val updatedValue = "Downloading File $i"
            Log.e(TAG, "Updated Value is :: $updatedValue")
            _uiState.value = updatedValue
            delay(2000)
        }
    }


    private fun flowStartedLog() {
        Log.e(TAG, "My Hot flow [ StateFlow ] is started.")
    }

}