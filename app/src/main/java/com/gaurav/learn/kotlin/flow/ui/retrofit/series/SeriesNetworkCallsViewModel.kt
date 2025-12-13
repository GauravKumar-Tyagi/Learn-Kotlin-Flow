package com.gaurav.learn.kotlin.flow.ui.retrofit.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.learn.kotlin.flow.data.api.ApiHelper
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelper
import com.gaurav.learn.kotlin.flow.data.model.ApiUser
import com.gaurav.learn.kotlin.flow.ui.base.UiState
import com.gaurav.learn.kotlin.flow.utils.DispatcherProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SeriesNetworkCallsViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ApiUser>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<ApiUser>>> = _uiState

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = UiState.Loading
            val allUsersFromApi = mutableListOf<ApiUser>()

            // API Call
            apiHelper.getUsers()
                .flatMapConcat { usersFromApi ->
                    allUsersFromApi.addAll(usersFromApi)
                    apiHelper.getMoreUsers()
                }
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect { moreUsersFromApi ->
                    allUsersFromApi.addAll(moreUsersFromApi)
                    _uiState.value = UiState.Success(allUsersFromApi)
                }

        }
    }


}