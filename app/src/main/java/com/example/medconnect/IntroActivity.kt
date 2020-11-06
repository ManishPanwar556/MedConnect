package com.example.medconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val doctorBtn = findViewById<Button>(R.id.doctorbtn)
        val patientBtn = findViewById<Button>(R.id.patientbtn)
        val hospitalBtn=findViewById<Button>(R.id.hospitalbtn)
        doctorBtn.setOnClickListener {
            gotoActivity(true,false,false)
        }
        patientBtn.setOnClickListener {
            gotoActivity(false,true,false)
        }
        hospitalBtn.setOnClickListener {
            gotoActivity(false,false,true)
        }

    }

    private fun gotoActivity(doctor: Boolean,patient:Boolean,hospital:Boolean) {
        val intent = Intent(this, SignInActivity::class.java)
        if (doctor) {
            intent.putExtra("data", "doctor")
        } else if(patient) {
            intent.putExtra("data", "patient")
        }
        else{
            intent.putExtra("data","hospital")
        }
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

    }
}