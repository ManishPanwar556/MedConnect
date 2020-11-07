package com.example.medconnect.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["uid"])
data class UserEntity(
    var name: String?,
    var sex: String?,
    var address: String?,
    var contact: String?,
    var time: String?,
    var age: String?,
    var date: String?,
    var uid:String
)