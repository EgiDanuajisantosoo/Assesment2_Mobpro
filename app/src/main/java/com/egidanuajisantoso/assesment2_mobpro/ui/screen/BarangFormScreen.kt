package com.egidanuajisantoso.assesment2_mobpro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.egidanuajisantoso.assesment2_mobpro.model.Barang
import com.egidanuajisantoso.assesment2_mobpro.util.BarangViewModelFactory
import android.app.Application
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangFormScreen(
    navController: NavController,
    action: String,
    barangId: Int?,
    viewModel: BarangViewModel = viewModel(
        factory = BarangViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val isEditMode = action == "edit"
    var nama by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val currentBarang by viewModel.currentBarang.collectAsState()
    // Load data jika mode edit
    LaunchedEffect(barangId) {
        if (isEditMode && barangId != null) {
            viewModel.loadBarangForEdit(barangId)
        }
    }

    LaunchedEffect(currentBarang) {
        currentBarang?.let { barang ->
            nama = barang.nama
            stok = barang.stok.toString()
            harga = barang.harga
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (isEditMode) "Edit Barang" else "Tambah Barang") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Tampilkan pesan error jika ada
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Barang") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null && nama.isBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = stok,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            stok = it
                        }
                    },
                    label = { Text("Stok") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null && stok.isBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = harga,
                    onValueChange = { harga = it },
                    label = { Text("Harga") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null && harga.isBlank()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Validasi input
                        when {
                            nama.isBlank() -> errorMessage = "Nama barang harus diisi"
                            stok.isBlank() -> errorMessage = "Stok harus diisi"
                            harga.isBlank() -> errorMessage = "Harga harus diisi"
                            else -> {
                                errorMessage = null
                                val stokValue = stok.toIntOrNull() ?: 0

                                // Gunakan coroutine scope untuk operasi database
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (isEditMode && barangId != null) {
                                        val updatedBarang = Barang(
                                            id = barangId,
                                            nama = nama,
                                            stok = stokValue,
                                            harga = harga
                                        )
                                        viewModel.updateBarang(updatedBarang)
                                    } else {
                                        val newBarang = Barang(
                                            nama = nama,
                                            stok = stokValue,
                                            harga = harga
                                        )
                                        viewModel.insertBarang(newBarang)
                                    }

                                    // Kembali ke halaman sebelumnya setelah operasi selesai
                                    withContext(Dispatchers.Main) {
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Text("Simpan")
                }
            }
        }
    )
}