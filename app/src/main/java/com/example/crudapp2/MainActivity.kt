package com.example.crudapp2

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.crudapp2.Adapters.AdapterMahasiswa
import com.example.crudapp2.Fragments.AddFragment
import com.example.crudapp2.Models.Mahasiswa
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import android.os.Parcelable

class MainActivity : AppCompatActivity() {

     lateinit var containerFrame: FrameLayout
    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val AddButton = findViewById<FloatingActionButton>(R.id.AddButton)
        val listView = findViewById<ListView>(R.id.listView)
        containerFrame = findViewById(R.id.containerFrame)

        val database = Firebase.database
        val getDBRef = database.getReference("/Mahasiswa")
        val mahasiswaList = mutableListOf<Mahasiswa>()

        val adapterMahasiswa = AdapterMahasiswa(this, R.layout.list_item_mahasiswa, mahasiswaList)
        listView.adapter = adapterMahasiswa // Set adapter to ListView

        val dialogEdDe = Dialog(this)
        dialogEdDe.setContentView(R.layout.dialog_edit_delete)


//            Get Data from FB and populate listView
            getDBRef.addValueEventListener(object : ValueEventListener{
//            OnDatachange these code repeat.
            override fun onDataChange(snapshot: DataSnapshot) {
                mahasiswaList.clear()
                    println("Ini adalah Datasnapshot :"+ snapshot.toString())

                for(snapshotMahasiswa in snapshot.children){
                    val key = snapshotMahasiswa.key ?: ""
                    val mahasiswa = snapshotMahasiswa.getValue(Mahasiswa::class.java)?.copy(key = key)

                    println("Ini adalah snapshot :"+ snapshotMahasiswa.toString())
                    println("Ini adalah snapshotKey :"+ key.toString())
                    println("Ini adalah mahasiswa :"+ mahasiswa.toString())

                    if (mahasiswa != null) {
                        mahasiswaList.add(mahasiswa) // Add tiap Mahasiswa to the list
                    }
                    adapterMahasiswa.notifyDataSetChanged() // Notify adapter data has changed
                }
            }
            override fun onCancelled(error: DatabaseError) {
                println("Failed to read Value." + error.toException())
            }
        })

//        Show Dialog Delete/Edit Options
        listView.setOnItemClickListener { parent, view, dataMhs, id ->
            dialogEdDe.show()
            val btnDelete = dialogEdDe.findViewById<Button>(R.id.btnDelete)
            val btnEdit = dialogEdDe.findViewById<Button>(R.id.btnEdit)

            val selectedMahasiswa = mahasiswaList[dataMhs]
            val getDataId = getDBRef.child(selectedMahasiswa.key) // Get Mahasiswa dengan key sama

//            Edit Actions
            btnEdit.setOnClickListener {
                val intent = Intent(this@MainActivity, UpdateActivity::class.java)

                intent.putExtra("key", selectedMahasiswa.key)
                intent.putExtra("nama", selectedMahasiswa.nama)
                intent.putExtra("nim", selectedMahasiswa.nim.toString())
                intent.putExtra("tempatLahir", selectedMahasiswa.tempatLahir)
                startActivity(intent)
            }

//            Delete Actions
            btnDelete.setOnClickListener {
                getDataId.removeValue()
                    .addOnSuccessListener {
                        dialogEdDe.hide()
                        Toast.makeText(this, "Data Mahasiswa Dihapus", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Data Mahasiswa Gagal di Hapus, Coba Lagi !", Toast.LENGTH_LONG).show()
                    }
            }
        }

        AddButton.setOnClickListener {
            containerFrame.visibility = View.VISIBLE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerFrame, AddFragment())
                .commit()
            Toast.makeText(this, "Menambahkan Mahasiswa", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        if(containerFrame.visibility == View.VISIBLE){
            containerFrame.visibility = View.GONE
        }
    }
}


