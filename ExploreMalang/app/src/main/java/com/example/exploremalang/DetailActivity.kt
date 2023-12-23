package com.example.exploremalang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class DetailActivity : AppCompatActivity() {

    private lateinit var tvNama: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var image: ImageView
    private lateinit var rvReview: RecyclerView
    private lateinit var btnReview: Button
    private lateinit var wisata: Wisata
    private var list: ArrayList<Review> = arrayListOf()
    private val reviewAdapter = ReviewListAdapter(list)
    private val dataReview = DataReview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvNama = findViewById(R.id.textView)
        tvDeskripsi = findViewById(R.id.textView2)
        image = findViewById(R.id.imageView2)
        rvReview = findViewById(R.id.rvReview)
        btnReview = findViewById(R.id.btnReview)

        btnReview.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("Wisata", wisata)
            startActivity(intent)
        }

        // Ambil data dari Intent
        wisata = intent.getParcelableExtra("Wisata")!!

        // Set data pada View
        tvNama.text = wisata.nama
        tvDeskripsi.text = wisata.deskripsi
        Glide.with(this)
            .load(wisata.foto)
            .into(image)

        // Inisialisasi Firebase database reference
        val wisataId = wisata.id
        val reviewRef =
            FirebaseDatabase.getInstance().getReference("Review").orderByChild("wisataId")
                .equalTo(wisataId)


// Inisialisasi ReviewAdapter dan ReviewList


// Set layout manager untuk RecyclerView
        rvReview.layoutManager = LinearLayoutManager(this)

// Set adapter untuk RecyclerView
        rvReview.adapter = reviewAdapter

// Listener untuk mengambil data dari Firebase database reference
        dataReview.get().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviewList = mutableListOf<Review>()

                for (childSnapshot in snapshot.children) {
                    val reviewMap = childSnapshot.value as? Map<String, Any>
                    val review = reviewMap?.let {
                        Review(
                            it["ulasan"] as String,
                            it["foto"] as String,
                            it["wisataId"] as String,
                            it["email"] as? String
                        )
                    }
                    review?.let {
                        if (it.wisataId == wisata.id) {
                            reviewList.add(it)
                        }
                    }
                }

                list.clear()
                list.addAll(reviewList)
                reviewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Error: ${error.message}")
            }
        })
    }
}
