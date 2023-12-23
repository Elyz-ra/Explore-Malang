package com.example.exploremalang

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    var ulasan: String = "",
    var foto: String = "",
    var wisataId: String = "",
    var email: String? = ""
) : Parcelable