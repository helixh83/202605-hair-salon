package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visit_history")
data class VisitHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val salonName: String,
    val visitDate: String,
    val designerName: String,
    val styleName: String,
    val price: Int,
    val status: String, // "REQUESTED", "APPROVED", "COMPLETED"
    val notes: String = "",
    val photoUrl: String? = null
)
