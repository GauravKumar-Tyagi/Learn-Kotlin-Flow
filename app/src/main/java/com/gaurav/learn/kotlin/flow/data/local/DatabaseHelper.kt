package com.gaurav.learn.kotlin.flow.data.local

import com.gaurav.learn.kotlin.flow.data.local.entity.User
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {

    fun getUsers(): Flow<List<User>>

    fun insertAll(users: List<User>): Flow<Unit>

}