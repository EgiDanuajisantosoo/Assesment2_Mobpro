package com.egidanuajisantoso.assesment2_mobpro.ui.screen

import com.egidanuajisantoso.assesment2_mobpro.database.BarangDao
import com.egidanuajisantoso.assesment2_mobpro.database.RecycleBarangDao
import com.egidanuajisantoso.assesment2_mobpro.model.Barang
import com.egidanuajisantoso.assesment2_mobpro.model.RecycleBarang
import kotlinx.coroutines.flow.Flow

class BarangRepository(private val barangDao: BarangDao,private val recycleBarangDao: RecycleBarangDao) {
    val allBarang: Flow<List<Barang>> = barangDao.getAllBarang()

    suspend fun insert(barang: Barang) {
        barangDao.insertBarang(barang)
    }

    suspend fun deletePermanently(recycleBarang: RecycleBarang) {
        recycleBarangDao.delete(recycleBarang)
    }

    suspend fun insertToRecycle(recycleBarang: RecycleBarang) {
        recycleBarangDao.insert(recycleBarang)
    }

    suspend fun deleteBarang(barang: Barang) {
        barangDao.deleteBarang(barang)
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

    suspend fun softDelete(barang: Barang) {
        // Pindahkan ke recycle bin
        val recycleBarang = RecycleBarang(
            originalId = barang.id,
            nama = barang.nama,
            stok = barang.stok,
            harga = barang.harga
        )
        recycleBarangDao.insert(recycleBarang)

        // Hapus dari tabel utama
        barangDao.deleteBarang(barang)
    }

    suspend fun restore(recycleBarang: RecycleBarang) {
        // Kembalikan ke tabel utama
        val barang = Barang(
            id = recycleBarang.originalId,
            nama = recycleBarang.nama,
            stok = recycleBarang.stok,
            harga = recycleBarang.harga
        )
        barangDao.insertBarang(barang)

        // Hapus dari recycle bin
        recycleBarangDao.delete(recycleBarang)
    }

    fun getAllRecycleBarang(): Flow<List<RecycleBarang>> {
        return recycleBarangDao.getAllRecycleBarang()
    }
}