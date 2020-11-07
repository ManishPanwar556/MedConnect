package com.example.medconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed(object:Runnable{
            override fun run() {
                if(FirebaseAuth.getInstance().currentUser!=null){
                    val uid= FirebaseAuth.getInstance().uid
                    val reference= FirebaseDatabase.getInstance().getReference().child("$uid")
                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                                val category = snapshot.value.toString()
                                goToActivity(category)


                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@SplashScreen,"Error", Toast.LENGTH_LONG).show()
                        }

                    })
                }
                else{
                    startActivity(Intent(this@SplashScreen,IntroActivity::class.java))
                    finish()
                }
            }

        },4000)
    }

    private fun goToActivity(category: String) {
        if (category.equals("Hospital")) {
            val intent = Intent(this, HospitalActivity::class.java)
            startActivity(intent)
        }
        else if(category.equals("Patient")){
            val intent= Intent(this,PatientActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent= Intent(this,DoctorActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}