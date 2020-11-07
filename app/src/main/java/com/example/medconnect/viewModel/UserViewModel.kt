package com.example.medconnect.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.medconnect.room.UserDatabase
import com.example.medconnect.room.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {
    private var db: UserDatabase
    var properties:LiveData<List<UserEntity>>
    init {
        db =
            Room.databaseBuilder(application.applicationContext, UserDatabase::class.java, "user_db")
                .fallbackToDestructiveMigration()
                .build()
        properties=getData()
    }
    private fun getData():LiveData<List<UserEntity>>{
      return db.userDao().getUser()
    }
    fun insertData(userEntity: UserEntity){
      val res=GlobalScope.launch(Dispatchers.IO) {
          db.userDao().insertUser(userEntity)
      }
      properties=getData()
    }
}