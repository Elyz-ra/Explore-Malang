package com.example.exploremalang

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference



class ReviewActivity : AppCompatActivity() {

    private lateinit var etUlasan: EditText
    private lateinit var btnAddImage: Button
    private lateinit var image: ImageView
    private lateinit var btnSave: Button

    private lateinit var wisata: Wisata
    private lateinit var database: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private val storage = FirebaseStorage.getInstance()

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        etUlasan = findViewById(R.id.editText3)
        btnAddImage = findViewById(R.id.button)
        image = findViewById(R.id.imageView3)
        btnSave = findViewById(R.id.btnSave)

        wisata = intent.getParcelableExtra("Wisata")!!
        database = Firebase.database.reference
        databaseReference = DataReview.getDatabaseReference()

        btnAddImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSave.setOnClickListener {
            val ulasan = etUlasan.text.toString().trim()
            val user = FirebaseAuth.getInstance().currentUser
            val userEmail = user?.email
            if (ulasan.isEmpty()) {
                Toast.makeText(this, "Ulasan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri == null) {
                Toast.makeText(this, "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val review = Review(ulasan, "", wisata.id, userEmail)
            val reviewId =
                databaseReference.child("Review").push().key!! // Menghapus child(wisata.id)
            val wisataId = wisata.id
            val storageRef = storage.reference
            val imageRef = storageRef.child("reviews").child(reviewId)

            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri: Uri ->
                        review.foto = uri.toString()
                        DataReview.insert(review)
                            .addOnSuccessListener {
                                val reviewRef =
                                    databaseReference.child("review-wisata").child(wisataId)
                                        .push() // Mengubah child(reviewId) menjadi push()
                                reviewRef.setValue(true)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Ulasan berhasil ditambahkan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(this, DetailActivity::class.java)
                                        intent.putExtra("Wisata", wisata)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Gagal menambahkan data",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Gagal menambahkan data", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
        }
    }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(image)
        }
    }
}

