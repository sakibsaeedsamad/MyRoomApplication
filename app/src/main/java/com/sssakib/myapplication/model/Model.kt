package com.sssakib.myapplication.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "age")
    val age: String?,
    @ColumnInfo(name = "phone")
    val phone: String?,
    @ColumnInfo(name = "gender")
    val gender: String?,
    @ColumnInfo(name = "location")
    val location: String?,
    @ColumnInfo(name = "image")
    val image: String?



)