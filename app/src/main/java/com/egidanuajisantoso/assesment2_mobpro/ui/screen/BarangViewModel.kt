package com.egidanuajisantoso.assesment2_mobpro.ui.screen
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.egidanuajisantoso.assesment2_mobpro.database.AppDatabase
import com.egidanuajisantoso.assesment2_mobpro.model.Barang
import com.egidanuajisantoso.assesment2_mobpro.model.RecycleBarang
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BarangViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BarangRepository
    val allBarang: Flow<List<Barang>>

    var showDialog by mutableStateOf(false)
        private set


    // Form fields
    var namaBarang by mutableStateOf("")
    var stokBarang by mutableStateOf("")
    var hargaBarang by mutableStateOf("")

    init {
        val barangDao = AppDatabase.getDatabase(application).barangDao()
        val recycleBarangDao = AppDatabase.getDatabase(application).recycleBarangDao()
        repository = BarangRepository(barangDao, recycleBarangDao)
        allBarang = repository.allBarang
    }

    fun softDeleteBarang(barang: Barang) = viewModelScope.launch {
        // 1. Pindahkan ke recycle bin
        val recycleBarang = RecycleBarang(
            originalId = barang.id,
            nama = barang.nama,
            stok = barang.stok,
            harga = barang.harga
        )
        repository.insertToRecycle(recycleBarang)

        // 2. Hapus dari tabel utama
        repository.deleteBarang(barang)
    }

    fun restoreBarang(recycleBarang: RecycleBarang) = viewModelScope.launch {
        repository.restore(recycleBarang)
    }

    val allRecycleBarang: Flow<List<RecycleBarang>> = repository.getAllRecycleBarang()

    fun hideDialog() {
        showDialog = false
    }

    private fun resetForm() {
        namaBarang = ""
        stokBarang = ""
        hargaBarang = ""
    }

    fun insertBarang() = viewModelScope.launch {
        val stok = stokBarang.toIntOrNull() ?: 0
        if (namaBarang.isNotBlank() && hargaBarang.isNotBlank() && stok >= 0) {
            val barang = Barang(
                nama = namaBarang,
                stok = stok,
                harga = hargaBarang
            )
            repository.insert(barang)
            hideDialog()
        }
    }

    fun deleteRecycleBarang(recycleBarang: RecycleBarang) = viewModelScope.launch {
        repository.deletePermanently(recycleBarang)
    }


    fun deleteBarang(barang: Barang) = viewModelScope.launch {
        repository.delete(barang)
    }

    private val _currentBarang = MutableStateFlow<Barang?>(null)
    val currentBarang: StateFlow<Barang?> = _currentBarang.asStateFlow()


    // Load barang for editing
    fun loadBarangForEdit(id: Int) {
        viewModelScope.launch {
            _currentBarang.value = repository.getBarangById(id)
        }
    }

    // Insert new barang
    suspend fun insertBarang(barang: Barang) {
        repository.insert(barang)
    }

    // Update existing barang
    suspend fun updateBarang(barang: Barang) {
        repository.update(barang)
    }
}