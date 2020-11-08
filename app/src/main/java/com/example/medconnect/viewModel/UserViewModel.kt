package com.example.medconnect.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.medconnect.room.MessageEntity
import com.example.medconnect.room.UserDatabase
import com.example.medconnect.room.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application) {

    private var db: UserDatabase
    private var _property=MutableLiveData<UserEntity>()
    val property:LiveData<UserEntity>
    get() =
        _property


    init {
        db =
            Room.databaseBuilder(application.applicationContext, UserDatabase::class.java, "user_db")
                .fallbackToDestructiveMigration()
                .build()

    }
    public fun getSingleData(id:String){
      _property.value= db.userDao().getSingleUser(id)
    }
    fun insertData(userEntity: UserEntity,id:String){
      GlobalScope.launch(Dispatchers.IO) {
          db.userDao().insertUser(userEntity)
          getSingleData(id)
      }

    }
    fun insertMessage(messageEntity: MessageEntity){
        GlobalScope.launch(Dispatchers.IO) {
            db.userDao().insertMessage(messageEntity)
        }
    }
    fun getAllMessage():LiveData<List<MessageEntity>>{
        return db.userDao().getAllMessage()
    }
    fun deleteUser(id:String){
        GlobalScope.launch(Dispatchers.IO) {
            db.userDao().delete(id)
        }
    }


}