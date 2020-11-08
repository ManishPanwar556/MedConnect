package com.example.medconnect.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class,MessageEntity::class],version = 2)
abstract class UserDatabase:RoomDatabase() {
    abstract fun userDao():UserInterFace
}