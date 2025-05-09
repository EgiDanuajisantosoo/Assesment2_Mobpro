package com.egidanuajisantoso.assesment2_mobpro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barang")
data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val stok: Int,
    val harga: String
)
