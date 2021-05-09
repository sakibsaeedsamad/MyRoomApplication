package com.sssakib.myapplication.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(User::class), version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase? {

            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder<UserDatabase>(

                    context.applicationContext,
                    UserDatabase::class.java,
                    "userdatabase"
                )
                    .allowMainThreadQueries()
                    .build()

            }
            return INSTANCE
        }

    }
}