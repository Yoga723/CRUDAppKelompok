package com.example.crudapp2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import com.example.crudapp2.Adapters.AdapterMahasiswa
import com.example.crudapp2.Models.Mahasiswa
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.util.Objects

class MainActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val AddButton = findViewById<FloatingActionButton>(R.id.AddButton)
        val listView = findViewById<ListView>(R.id.listView)

        val database = Firebase.database
        val getDBRef = database.getReference("/Mahasiswa")
        val mahasiswaList = mutableListOf<Mahasiswa>()

        val adapterMahasiswa = AdapterMahasiswa(this, R.layout.list_item_mahasiswa, mahasiswaList)
        listView.adapter = adapterMahasiswa // Set adapter to ListView

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
                        mahasiswaList.add(mahasiswa) // Add each Mahasiswa to the list
                    }
                    adapterMahasiswa.notifyDataSetChanged() // Notify the adapter that the data has changed
                }
            }
            override fun onCancelled(error: DatabaseError) {
                println("Failed to read Value." + error.toException())
            }
        })

    }
}