package com.gaurav.learn.kotlin.flow.data.api

import com.gaurav.learn.kotlin.flow.data.model.ApiUser
import kotlinx.coroutines.flow.Flow


interface ApiHelper {

    fun getUsers(): Flow<List<ApiUser>>

    fun getMoreUsers(): Flow<List<ApiUser>>

    fun getUsersWithError(): Flow<List<ApiUser>>

}