package com.example.exploremalang

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

object DataReview {
    private var databaseReference: DatabaseReference

    init {
        val db = FirebaseDatabase.getInstance("https://explore-malang-eff3b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = db.getReference(Review::class.simpleName!!)
    }

    fun insert(note: Review): Task<Void> {
        return databaseReference.push().setValue(note)
    }

    fun update(key: String, note: Review): Task<Void> {
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

    fun getDatabaseReference(): DatabaseReference {
        return databaseReference
    }
}
