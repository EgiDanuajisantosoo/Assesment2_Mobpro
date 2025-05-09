package com.egidanuajisantoso.assesment2_mobpro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.egidanuajisantoso.assesment2_mobpro.R
import com.egidanuajisantoso.assesment2_mobpro.model.RecycleBarang
import com.egidanuajisantoso.assesment2_mobpro.ui.screen.BarangViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    navController: NavController,
    viewModel: BarangViewModel
) {
    val recycleBarang by viewModel.allRecycleBarang.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedRecycleBarang by remember { mutableStateOf<RecycleBarang?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "recycle bin") },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (recycleBarang.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Recycle bin kosong", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn {
                    items(recycleBarang) { item ->
                        RecycleBarangItem(
                            recycleBarang = item,
                            onRestoreClick = {
                                viewModel.restoreBarang(item)
                            },
                            onDeleteClick = {
                                selectedRecycleBarang = item
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog && selectedRecycleBarang != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Hapus Permanen") },
            text = { Text("Apakah Anda yakin ingin menghapus permanen barang ini?") },
            confirmButton = {
                TextButton(onClick = {
                    selectedRecycleBarang?.let { item ->
                        viewModel.viewModelScope.launch {
                            viewModel.deleteRecycleBarang(item) // Add this function to ViewModel
                        }
                    }
                    showDialog = false
                }) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun RecycleBarangItem(
    recycleBarang: RecycleBarang,
    onRestoreClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nama: ${recycleBarang.nama}",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Stok: ${recycleBarang.stok}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Harga: ${recycleBarang.harga}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Dihapus pada: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(Date(recycleBarang.deletedAt))}",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onRestoreClick) {
                    Text("Kembalikan")
                }
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus Permanen")
                }
            }
        }
    }
}