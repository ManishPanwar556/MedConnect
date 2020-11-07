package com.example.medconnect.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.medconnect.R
import com.firebase.ui.auth.AuthUI

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        val logoutbtn=findViewById<Button>(R.id.logout)
        logoutbtn.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent= Intent(this, IntroActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}