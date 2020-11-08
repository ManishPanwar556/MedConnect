package com.example.medconnect.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.medconnect.R
import com.firebase.ui.auth.AuthUI

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_barcode,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.logoutmenu2->{
                performLogout()
                true
            }
            R.id.scancode->{
                openCamera()
                true
            }
            else->{
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun openCamera(){

    }

    private fun performLogout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            if(it.isSuccessful){
                val intent=Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }
        }
    }
}