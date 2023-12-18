package com.example.crudapp2.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.example.crudapp2.R
import com.google.firebase.Firebase
import com.google.firebase.database.database

class AddFragment : Fragment() {

    private lateinit var containerFrame: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        val database = Firebase.database
        val getDBRef = database.getReference("/Mahasiswa")

        val inputNama = view.findViewById<EditText>(R.id.inputNama)
        val inputNIM = view.findViewById<EditText>(R.id.inputNIM)
        val inputTempatLahir = view.findViewById<EditText>(R.id.inputTempatLahir)
        val tambahMhsBtn = view.findViewById<Button>(R.id.tambahMhsBtn)
        val KembaliBtn = view.findViewById<Button>(R.id.KembaliBtn)

        tambahMhsBtn.setOnClickListener {
            if(inputNama.text.toString().isEmpty() || inputNama.text.length <= 3 || inputNIM.text.toString().isEmpty() || inputNIM.text.length <= 3 || inputTempatLahir.text.toString().isEmpty() || inputTempatLahir.text.length <= 3){
                inputNama.error = "Nama tidak boleh Kosong/Kurang dari 3"
                inputNIM.error = "NIM tidak boleh Kosong/Kurang dari 3"
                inputTempatLahir.error = "Tempat Lahir tidak boleh Kosong/Kurang dari 3"
            }else{
                val nimString = inputNIM.text.toString()    // Convert to String
//               Check if not empty ganti ka number men bisa, men hente 0
                val nim = if (nimString.isNotEmpty()) nimString.toLong() else 0
                val profileMahasiswa = object {
                    val nama = inputNama.text.toString()
                    val NIM = nim
                    val TempatLahir = inputTempatLahir.text.toString()
                }

                getDBRef.push().setValue(profileMahasiswa)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Berhasil Menambahkan Mahasiswa !", Toast.LENGTH_SHORT).show()
                        closeFragment()
                    }
            }
        }
        KembaliBtn.setOnClickListener {
            closeFragment()
        }

        // Inflate the layout for this fragment
        return view
    }
    fun closeFragment(){
        containerFrame = requireActivity().findViewById(R.id.containerFrame)
        if(containerFrame.visibility == View.VISIBLE){
            containerFrame.visibility = View.GONE
        }
    }

}