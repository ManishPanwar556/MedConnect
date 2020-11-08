package com.example.medconnect.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.medconnect.R
import com.example.medconnect.viewModel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class CustomDialog(context:Context,var id:String): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog)
        val reference= FirebaseDatabase.getInstance().getReference().child("Patients")
        val medicine=findViewById<EditText>(R.id.medicine)
        val savebtn=findViewById<MaterialButton>(R.id.saveBtn)
        Toast.makeText(context,"$id",Toast.LENGTH_SHORT).show()
        savebtn.setOnClickListener {

            reference.child("$id").child("prescription").setValue(medicine.text.toString())
            dismiss()
        }
    }
}