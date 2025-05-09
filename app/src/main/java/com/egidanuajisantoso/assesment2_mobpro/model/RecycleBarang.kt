package com.egidanuajisantoso.assesment2_mobpro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recycle_barang")
data class RecycleBarang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val originalId: Int, // ID asli dari tabel barang
    val nama: String,
    val stok: Int,
    val harga: String,
    val deletedAt: Long = System.currentTimeMillis() // Waktu penghapusan
)
