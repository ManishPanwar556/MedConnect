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
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medconnect.MyAdapter
import com.google.android.material.card.MaterialCardView
import com.example.medconnect.R
import com.example.medconnect.room.MessageEntity
import com.example.medconnect.room.UserEntity
import com.example.medconnect.viewModel.UserViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import org.w3c.dom.Text

class DoctorActivity : AppCompatActivity() {
    val viewModel by lazy {
        UserViewModel(application)
    }
    var id: String = ""

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        val prescribeBtn = findViewById<MaterialButton>(R.id.prescribebtn)

        prescribeBtn.setOnClickListener {
            val dialog = CustomDialog(this, id)
            dialog.show()
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
                        getDataFromFirebase(barcode.displayValue)
                    }
                }
            }
        }
    }

    private fun getDataFromFirebase(displayValue: String?) {
        val ref = FirebaseDatabase.getInstance().getReference("Patients")
        if (displayValue != null) {
            id = displayValue
            val child = ref.child("$displayValue")
            child.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val patientName = snapshot.child("name").value?.toString()
                    val patientAddress = snapshot.child("address").value?.toString()
                    val age = snapshot.child("age").value?.toString()
                    val sex = snapshot.child("sex").value?.toString()
                    val date = snapshot.child("date").value?.toString()
                    val symptoms = snapshot.child("symptoms").value?.toString()
                    val contact = snapshot.child("contact").value?.toString()
                    val time = snapshot.child("time").value?.toString()
                    applyData(
                        patientName,
                        patientAddress,
                        age,
                        contact,
                        symptoms,
                        date,
                        sex,
                        time,
                        displayValue
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DoctorActivity, "Erroe ${error}", Toast.LENGTH_SHORT).show()
                }

            })
        } else {

            Toast.makeText(this, "Not able To identify the qr code", Toast.LENGTH_SHORT).show()
        }

        ref.child("prescription").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<String>()
                snapshot.children.forEach {
                    list.add(it.value.toString())
                }
                val adapter = ArrayAdapter<String>(
                    this@DoctorActivity,
                    android.R.layout.simple_list_item_1,
                    list
                )
                val listView = findViewById<ListView>(R.id.listView)
                listView.adapter = adapter
                val prescription = snapshot.value?.toString()


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DoctorActivity, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun applyData(
        patientName: String?,
        patientAddress: String?,
        age: String?,
        contact: String?,
        symptoms: String?,
        pdate: String?,
        sex: String?,
        time: String?,
        displayValue: String
    ) {
        val prescribeBtn = findViewById<MaterialButton>(R.id.prescribebtn)
        val cardView = findViewById<MaterialCardView>(R.id.cardView)
        val cardView2 = findViewById<MaterialCardView>(R.id.cardView2)
        val infoText = findViewById<TextView>(R.id.infoTextView)
        val pname = findViewById<TextView>(R.id.patientName)
        val patientAge = findViewById<TextView>(R.id.patientAge)
        val patientSex = findViewById<TextView>(R.id.patientSex)
        val date = findViewById<TextView>(R.id.patientDate)
        val patientInfo = findViewById<TextView>(R.id.patientInfo)
        val rev = findViewById<ListView>(R.id.listView)
        rev.visibility = View.VISIBLE
        prescribeBtn.visibility = View.VISIBLE
        infoText.visibility = View.GONE
        cardView.visibility = View.VISIBLE
        cardView2.visibility = View.VISIBLE
        pname.text = patientName
        patientAge.text = age
        patientSex.text = sex
        date.text = pdate
        patientInfo.text =
            "${patientName} has been suffering from ${symptoms} and needs urgent care"


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_barcode, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.logoutmenu2 -> {
                performLogout()
                true
            }
            R.id.scancode -> {
                openCamera()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }
}