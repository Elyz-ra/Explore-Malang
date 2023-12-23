package com.example.exploremalang

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wisata(
    var nama: String = "",
    var deskripsi: String = "",
    var foto: String = "",
    var kategori: String = "",
    var lokasi: String = "",
    var id: String= "",
) : Parcelable
