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
    @Query("Delete from userEntity where uid=:id")
    suspend fun delete(id:String)
    @Query("Select *from userentity where uid=:id")
    fun getSingleUser(id:String):UserEntity
    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity)
    @Query("Select *from messageentity")
    fun getAllMessage():LiveData<List<MessageEntity>>
}