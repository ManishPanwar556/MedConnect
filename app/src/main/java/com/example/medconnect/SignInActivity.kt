package com.example.medconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

class SignInActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGNIN = 123
    }
    lateinit var data:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val intentValue=intent.getStringExtra("data")
        if(intentValue!=null){
            data=intentValue
        }

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .build(),
            RC_SIGNIN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SIGNIN){
            val response=IdpResponse.fromResultIntent(data)
            if(resultCode== RESULT_OK){
                 Toast.makeText(this,"Successfully Signed In",Toast.LENGTH_LONG).show()
                if(data!=null){
                    Toast.makeText(this,"$data.",Toast.LENGTH_LONG).show()
                    if(data.equals("hospital")){
                        val intent=Intent(this,HospitalActivity::class.java)
                        startActivity(intent)
                    }
                    else if(data.equals("doctor")){
                        val intent=Intent(this,DoctorActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        val intent=Intent(this,PatientActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
            }
            else{
                Toast.makeText(this,"SignIn Error",Toast.LENGTH_LONG).show()
            }
        }
    }
}