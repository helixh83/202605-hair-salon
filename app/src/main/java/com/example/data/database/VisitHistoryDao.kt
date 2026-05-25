package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitHistoryDao {
    @Query("SELECT * FROM visit_history ORDER BY visitDate DESC, id DESC")
    fun getAllVisits(): Flow<List<VisitHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitHistory)

    @Update
    suspend fun updateVisit(visit: VisitHistory)

    @Delete
    suspend fun deleteVisit(visit: VisitHistory)

    @Query("SELECT * FROM visit_history WHERE id = :id LIMIT 1")
    suspend fun getVisitById(id: Int): VisitHistory?
}
