package com.example.exploremalang

import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

object DataWisata {
    private var databaseReference: DatabaseReference

    init {
        val db = FirebaseDatabase.getInstance("https://explore-malang-eff3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = db.getReference(Wisata::class.simpleName!!)
    }

    fun insert(wisata: Wisata): Task<Void> {
        return databaseReference.push().setValue(wisata)
    }

    fun update(key: String, note: Wisata): Task<Void> {
        return databaseReference.child(key).setValue(note)
    }

    fun delete(key: String): Task<Void> {
        return databaseReference.child(key).removeValue()
    }

    fun get(): Query {
        return databaseReference.orderByKey()
    }

    fun getAll(key: String): Task<DataSnapshot> {
        return databaseReference.child(key).get()
    }

}