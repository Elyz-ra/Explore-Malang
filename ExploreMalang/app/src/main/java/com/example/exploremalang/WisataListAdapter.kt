package com.example.exploremalang

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class WisataListAdapter(private val listWisata: ArrayList<Wisata>) : RecyclerView.Adapter<WisataListAdapter.ListViewHolder>() {
    private var filteredList = listWisata
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val wisata = filteredList[position]
        Glide.with(holder.itemView.context)
            .load(wisata.foto)
            .apply(RequestOptions())
            .into(holder.img)
        holder.tvNama.text = wisata.nama
        holder.tvLokasi.text = wisata.lokasi

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("Wisata", wisata)
            context.startActivity(intent)
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.imageView)
        var tvNama: TextView = itemView.findViewById(R.id.tvNama)
        var tvLokasi: TextView = itemView.findViewById(R.id.tvLokasi)
    }

    fun filterList(filteredNames: List<Wisata>) {
        filteredList = filteredNames as ArrayList<Wisata>
        notifyDataSetChanged()
    }

}