package com.gaurav.learn.kotlin.flow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gaurav.learn.kotlin.flow.data.local.dao.UserDao
import com.gaurav.learn.kotlin.flow.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}