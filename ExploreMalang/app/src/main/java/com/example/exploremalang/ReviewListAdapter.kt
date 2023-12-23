package com.example.exploremalang

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class ReviewListAdapter(private val listReview: ArrayList<Review>) : RecyclerView.Adapter<ReviewListAdapter.ListViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_review, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val review = listReview[position]
        holder.tvUlasan.text = review.ulasan
        holder.tvEmail.text = review.email
        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(review.foto)
            .apply(RequestOptions())
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ReviewDetailActivity::class.java)
            intent.putExtra("Review", review)
            context.startActivity(intent)
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUlasan: TextView = itemView.findViewById(R.id.tvUlasan)
        var img: ImageView = itemView.findViewById(R.id.imgReview)
        var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
    }
}
