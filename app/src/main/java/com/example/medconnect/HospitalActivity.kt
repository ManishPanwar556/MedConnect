package com.example.medconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class HospitalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hospital_activity)
        val usertext=findViewById<TextView>(R.id.userNameText)
        val username=FirebaseAuth.getInstance().currentUser?.displayName
        if(username!=null){
            usertext.text=username
        }
        val addbutton=findViewById<Button>(R.id.enternewbtn)
        val logoutbtn=findViewById<Button>(R.id.logout)
        addbutton.setOnClickListener {
             startActivity(Intent(this,FormActivity::class.java))
        }
        logoutbtn.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent=Intent(this,IntroActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}