package com.egidanuajisantoso.assesment2_mobpro.database

import androidx.room.*
import com.egidanuajisantoso.assesment2_mobpro.model.RecycleBarang
import kotlinx.coroutines.flow.Flow

@Dao
interface RecycleBarangDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recycleBarang: RecycleBarang)

    @Delete
    suspend fun delete(recycleBarang: RecycleBarang)

    @Query("SELECT * FROM recycle_barang ORDER BY deletedAt DESC")
    fun getAllRecycleBarang(): Flow<List<RecycleBarang>>

    @Query("SELECT * FROM recycle_barang WHERE id = :id")
    suspend fun getRecycleBarangById(id: Int): RecycleBarang?
}