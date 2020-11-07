package com.example.medconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class IntroActivity : AppCompatActivity() {
    lateinit var status: String
    companion object{
        const val RC_SIGNIN=123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val doctorBtn = findViewById<Button>(R.id.doctorbtn)
        val patientBtn = findViewById<Button>(R.id.patientbtn)
        val hospitalBtn = findViewById<Button>(R.id.hospitalbtn)
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )
        doctorBtn.setOnClickListener {
            status = "Doctor"
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                    .build(),
                RC_SIGNIN
            )

        }
        patientBtn.setOnClickListener {
            status = "Patient"
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                    .build(),
                RC_SIGNIN
            )

        }
        hospitalBtn.setOnClickListener {
            status = "Hospital"
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                    .build(),
                RC_SIGNIN
            )

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNIN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successfully Signed In", Toast.LENGTH_LONG).show()
                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference().child("${FirebaseAuth.getInstance().uid}")
                ref.setValue(status)
                goToActivity(status)

            } else {
                Toast.makeText(this, "SignIn Error", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun goToActivity(category:String){
        if (category.equals("Hospital")) {
            val intent = Intent(this, HospitalActivity::class.java)
            startActivity(intent)
        }
        else if(category.equals("Patient")){
            val intent=Intent(this,PatientActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent=Intent(this,DoctorActivity::class.java)
            startActivity(intent)
        }
        finish()
    }


}