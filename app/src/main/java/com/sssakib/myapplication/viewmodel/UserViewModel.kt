package com.sssakib.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sssakib.myapplication.model.User
import com.sssakib.myapplication.model.UserDatabase
import org.w3c.dom.Entity
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application){

    lateinit var allUsers: MutableLiveData<List<User>>

    init{
        allUsers = MutableLiveData()
        getAllUsers()
    }

    fun getAllUsersObservers() : MutableLiveData<List<User>>{
        return allUsers
    }

    fun getAllUsers(){
        val userDao = UserDatabase.getDatabase(getApplication())?.userDao()
        val list = userDao?.getAllUserInfo()
        allUsers.postValue(list)
    }
    fun insertUserInfo(user: User){
        val userDao = UserDatabase.getDatabase(getApplication())?.userDao()
        userDao?.insertUser(user)
        getAllUsers()
    }

    fun updateUserInfo(user: User){
        val userDao = UserDatabase.getDatabase(getApplication())?.userDao()
        userDao?.updateUser(user)
        getAllUsers()
    }
    fun deleteUserInfo(user: User){
        val userDao = UserDatabase.getDatabase(getApplication())?.userDao()
        userDao?.deleteUser(user)
        getAllUsers()
    }


}