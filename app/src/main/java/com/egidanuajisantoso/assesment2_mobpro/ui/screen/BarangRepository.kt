package com.egidanuajisantoso.assesment2_mobpro.ui.screen

import com.egidanuajisantoso.assesment2_mobpro.database.BarangDao
import com.egidanuajisantoso.assesment2_mobpro.model.Barang
import kotlinx.coroutines.flow.Flow

class BarangRepository(private val barangDao: BarangDao) {
    val allBarang: Flow<List<Barang>> = barangDao.getAllBarang()

    suspend fun insert(barang: Barang) {
        barangDao.insertBarang(barang)
    }

    suspend fun update(barang: Barang) {
        barangDao.updateBarang(barang)
    }

    suspend fun delete(barang: Barang) {
        barangDao.deleteBarang(barang)
    }

    suspend fun getBarangById(id: Int): Barang? {
        return barangDao.getBarangById(id)
    }
}