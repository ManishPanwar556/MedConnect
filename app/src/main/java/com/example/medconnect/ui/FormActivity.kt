package com.example.medconnect.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.medconnect.R
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val saveButton = findViewById<Button>(R.id.saveBtn)
        val name = findViewById<EditText>(R.id.nameEditText)
        val sex = findViewById<EditText>(R.id.sexEditText)
        val date = findViewById<EditText>(R.id.dateEditText)
        val time = findViewById<EditText>(R.id.timeeditText)
        val address = findViewById<EditText>(R.id.addressEditText)
        val contact = findViewById<EditText>(R.id.contactEditText)
        val age = findViewById<EditText>(R.id.ageEditText)
        saveButton.setOnClickListener {
            if (name.text.isEmpty() || sex.text.isEmpty() || date.text.isEmpty() || time.text.isEmpty() || address.text.isEmpty() || contact.text.isEmpty() || age.text.isEmpty()||contact.text.toString().length!=10) {
                Toast.makeText(this,"Fill The Empty Field",Toast.LENGTH_SHORT).show()
            }
            else{
                val database=FirebaseDatabase.getInstance()
                val uuid=UUID.randomUUID().toString()
                val map=HashMap<String,String>()
                map.put("name",name.text.toString())
                map.put("sex",sex.text.toString())
                map.put("date",date.text.toString())
                map.put("time",time.text.toString())
                map.put("address",address.text.toString())
                map.put("age",age.text.toString())
                map.put("contact",contact.text.toString())
                val ref=database.getReference().child("Patients").child("$uuid")
                ref.setValue(map).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Patient Record Successfully Added",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HospitalActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Ooooops Error",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}