package com.example.medconnect.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserInterFace {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user:UserEntity)
    @Query("Select *from userEntity")
    fun getUser():LiveData<List<UserEntity>>
}