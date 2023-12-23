package com.example.exploremalang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var rvKontak: RecyclerView
    private val list: ArrayList<Wisata> = arrayListOf()
    private lateinit var searchBar: EditText
    private lateinit var dropDown: Spinner
    private val wisataAdapter = WisataListAdapter(list)
    private val dataWisata = DataWisata // tambahkan inisialisasi untuk dataWisata
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchBar = findViewById(R.id.searchBar)
        dropDown = findViewById(R.id.dropDown)
        rvKontak = findViewById(R.id.rvWisata)
        rvKontak.setHasFixedSize(true)

        // set up the spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.kategori_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dropDown.adapter = adapter
        }

        // add a listener for the search bar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filter = s.toString().toLowerCase(Locale.ROOT)
                dataWisata.get().get().addOnSuccessListener { snapshot ->
                    val filteredList =
                        snapshot.children.mapNotNull { it.getValue(Wisata::class.java) }
                            .filter { wisata ->
                                wisata.nama.toLowerCase(Locale.ROOT).contains(filter) ||
                                        wisata.lokasi.toLowerCase(Locale.ROOT).contains(filter)
                            }
                    wisataAdapter.filterList(filteredList)
                }
            }
        })

        // add a listener for the spinner
        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val filter = parent.getItemAtPosition(pos).toString()
                dataWisata.get().get().addOnSuccessListener { snapshot ->
                    val filteredList =
                        snapshot.children.mapNotNull { it.getValue(Wisata::class.java) }
                            .filter { wisata ->
                                if (filter == "Semua Kategori") {
                                    true
                                } else {
                                    wisata.kategori == filter
                                }
                            }
                    wisataAdapter.filterList(filteredList)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        showRecyclerList()
        dataWisata.get().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val wisataList = snapshot.children.mapNotNull { it.getValue(Wisata::class.java) }
                list.clear()
                list.addAll(wisataList)
                wisataAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Error: ${error.message}")
            }
        })
    }

    private fun showRecyclerList() {
        rvKontak.layoutManager = LinearLayoutManager(this)
        rvKontak.adapter = wisataAdapter
    }
}
