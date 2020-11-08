package com.example.medconnect.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.medconnect.R
import com.example.medconnect.room.UserEntity
import com.example.medconnect.viewModel.UserViewModel
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class PatientActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    val viewModel by lazy {
        UserViewModel(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)
        viewModel.properties.observe(this, Observer { list ->
            list.forEach {
                applyData(it.name, it.address, it.contact, it.date, it.age, it.sex, it.time)
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.barCode -> {
                openCamera()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_item, menu)
        return true
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            getDataFromQrCode(imageBitmap)
        }

    }

    private fun getDataFromQrCode(imageBitmap: Bitmap) {
        val options =
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        val image = InputImage.fromBitmap(imageBitmap, 0)
        val scanner = BarcodeScanning.getClient(options)
        val result = scanner.process(image).addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                when (barcode.valueType) {
                    Barcode.TYPE_TEXT -> {
                        Toast.makeText(this, "${barcode.displayValue}", Toast.LENGTH_SHORT).show()
                        getData(barcode.displayValue)
                    }
                }
            }
        }

    }

    private fun getData(displayValue: String?) {

        if (displayValue != null) {
            val firebaseDatabase =
                FirebaseDatabase.getInstance().getReference().child("Patients")
                    .child("$displayValue")
            firebaseDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value?.toString()
                    val address = snapshot.child("address").value?.toString()
                    val contact = snapshot.child("contact").value?.toString()
                    val date = snapshot.child("date").value?.toString()
                    val age = snapshot.child("age").value?.toString()
                    val sex = snapshot.child("sex").value?.toString()
                    val time = snapshot.child("time").value?.toString()
                    Toast.makeText(this@PatientActivity, "$name", Toast.LENGTH_SHORT).show()
                    val user =
                        UserEntity(name, sex, address, contact, time, age, date, displayValue)
                    viewModel.insertData(user)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PatientActivity, "${error}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun applyData(
        pname: String?,
        address: String?,
        contact: String?,
        patientDate: String?,
        patientAge: String?,
        patientSex: String?,
        time: String?
    ) {
        val cardView = findViewById<MaterialCardView>(R.id.cardView)
        val profile = findViewById<ImageView>(R.id.profile)
        cardView.visibility = View.VISIBLE
        profile.visibility = View.VISIBLE
        val name = findViewById<TextView>(R.id.patientName)
        val age = findViewById<TextView>(R.id.patientAge)
        val sex = findViewById<TextView>(R.id.patientSex)
        val date = findViewById<TextView>(R.id.patientDate)
        name.text = pname
        age.text = patientAge
        sex.text = patientSex
        date.text = patientDate
    }

}