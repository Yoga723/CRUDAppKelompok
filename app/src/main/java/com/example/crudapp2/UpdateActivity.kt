package com.example.crudapp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.crudapp2.Models.Mahasiswa
import com.google.firebase.Firebase
import com.google.firebase.database.database

class UpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val editMhsBtn = findViewById<Button>(R.id.editMhsBtn)
        val KembaliBtn = findViewById<Button>(R.id.KembaliBtn)
        val inputNama = findViewById<EditText>(R.id.inputNama)
        val inputNIM = findViewById<EditText>(R.id.inputNIM)
        val inputTempatLahir = findViewById<EditText>(R.id.inputTempatLahir)

        val database = Firebase.database
        val getDBRef = database.getReference("/Mahasiswa")

//        Received Data
        val key: String? = intent.getStringExtra("key")
        val nama: String? = intent.getStringExtra("nama")
        val nim: String? = intent.getStringExtra("nim")
        val tempatLahir: String? = intent.getStringExtra("tempatLahir")

        inputNama.setText(nama.toString())
        inputNIM.setText(nim.toString())
        inputTempatLahir.setText(tempatLahir.toString())

//        println("Ini adalah nama :"+ nama.toString())
//        println("Ini adalah nim :"+ nim.toString())

        editMhsBtn.setOnClickListener {
            if(inputNama.text.toString().isEmpty() || inputNama.text.length <= 3 || inputNIM.text.toString().isEmpty() || inputNIM.text.length <= 3 || inputTempatLahir.text.toString().isEmpty() || inputTempatLahir.text.length <= 3){
                inputNama.error = "Nama tidak boleh Kosong/Kurang dari 3"
                inputNIM.error = "NIM tidak boleh Kosong/Kurang dari 3"
                inputTempatLahir.error = "Tempat Lahir tidak boleh Kosong/Kurang dari 3"
            }else{
                val getData = getDBRef.child(key.toString())

                val nimString = inputNIM.text.toString()    // Convert to String
//               Check if not empty ganti ka number men bisa, men hente 0
                val newNim = if (nimString.isNotEmpty()) nimString.toLong() else 0
                val profileMahasiswa = object {
                    val nama = inputNama.text.toString()
                    val NIM = newNim
                    val TempatLahir = inputTempatLahir.text.toString()
                }
                getData.setValue(profileMahasiswa)
                    .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil Update Data Mahasiswa", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdateActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                    .addOnCanceledListener {
                        Toast.makeText(this, "Gagal Update Data Mahasiswa", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        KembaliBtn.setOnClickListener {
            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}