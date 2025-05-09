package com.egidanuajisantoso.assesment2_mobpro.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.egidanuajisantoso.assesment2_mobpro.model.Barang
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertBarang(barang: Barang)

        @Update
        suspend fun updateBarang(barang: Barang)

        @Delete
        suspend fun deleteBarang(barang: Barang)

        @Query("SELECT * FROM barang ORDER BY nama ASC")
        fun getAllBarang(): Flow<List<Barang>>

        @Query("SELECT * FROM barang WHERE id = :id")
        suspend fun getBarangById(id: Int): Barang?

        @Query("SELECT * FROM barang ORDER BY id DESC")
        fun getBarang() : Flow<List<Barang>>
}