package com.example.crudapp2.Adapters
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.crudapp2.Models.Mahasiswa
import com.example.crudapp2.R

class AdapterMahasiswa(context: Context, resource: Int, objects: List<Mahasiswa>) :
    ArrayAdapter<Mahasiswa>(context, resource, objects) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_mahasiswa, parent, false)

        val mahasiswa = getItem(position)
        val textViewKey = view.findViewById<TextView>(R.id.textViewKey)
        val textViewNama = view.findViewById<TextView>(R.id.textViewNama)
        val textViewNIM = view.findViewById<TextView>(R.id.textViewNIM)
        val textViewTempatLahir = view.findViewById<TextView>(R.id.textViewTempatLahir)

        textViewNama.text = "Nama: ${mahasiswa?.nama}"
        textViewKey.text = "Key: ${mahasiswa?.key}"
        textViewNIM.text = "NIM: ${mahasiswa?.nim}"
        textViewTempatLahir.text = "Tempat Lahir: ${mahasiswa?.tempatLahir}"

        return view
    }
}