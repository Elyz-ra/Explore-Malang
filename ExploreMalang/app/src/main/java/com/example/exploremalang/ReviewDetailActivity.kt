package com.example.exploremalang

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ReviewDetailActivity : AppCompatActivity() {
    private lateinit var imageReview: ImageView
    private lateinit var btnDelete: Button
    private lateinit var tvUlasan: TextView
    private lateinit var review: Review

    private val dataReview = DataReview
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)

        imageReview = findViewById(R.id.imageReview)
        btnDelete = findViewById(R.id.btnDelete)
        tvUlasan = findViewById(R.id.tvUlasan)
        storageReference = FirebaseStorage.getInstance().reference

        review = intent.getParcelableExtra("Review")!!

        tvUlasan.text = review.ulasan
        Glide.with(this)
            .load(review.foto)
            .into(imageReview)

        btnDelete.setOnClickListener {
            deleteReviewFromDatabase(review)
        }
    }

    private fun showImage(bitmap: Bitmap) {
        // Tampilkan gambar di ImageView atau lakukan tindakan sesuai kebutuhan
        imageReview.setImageBitmap(bitmap)
    }


    private fun deleteReviewFromDatabase(review: Review) {
        val reviewRef = dataReview.getDatabaseReference()
        val query = reviewRef.orderByChild("ulasan").equalTo(review.ulasan)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val reviewKey = childSnapshot.key
                    if (reviewKey != null) {
                        reviewRef.child(reviewKey).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@ReviewDetailActivity,
                                    "Review deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@ReviewDetailActivity,
                                    "Failed to delete review",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ReviewDetailActivity,
                    "Failed to delete review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

