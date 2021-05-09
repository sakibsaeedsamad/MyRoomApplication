package com.sssakib.myapplication.model

import androidx.room.*

@Dao
interface UserDao {


    @Query("SELECT * from user ORDER BY id DESC ")
    fun getAllUserInfo(): List<User>?


    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user: User)

}