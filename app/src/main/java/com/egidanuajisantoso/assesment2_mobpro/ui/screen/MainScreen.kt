package com.egidanuajisantoso.assesment2_mobpro.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egidanuajisantoso.assesment2_mobpro.R
import com.egidanuajisantoso.assesment2_mobpro.ui.theme.Assesment2_MobproTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.nama_aplikasi))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { padding ->
        ScreenContent(modifier = Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    val barangList = listOf(
        "Sabun", "Shampoo", "Minyak Goreng", "Gula", "Garam",
        "Susu", "Mie Instan", "Tepung Terigu", "Beras", "Kopi"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn {
            items(barangList) { namaBarang ->
                BarangCard(namaBarang = namaBarang, stok = 20, harga = "Rp10.000")
            }
        }
    }
}

@Composable
fun BarangCard(namaBarang: String, stok: Int, harga: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "Nama Barang: $namaBarang", fontWeight = FontWeight.Bold)
            Text(text = "Stok: $stok")
            Text(text = "Harga: $harga")
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = {  }) {
                    Text("Edit", color = Color.Blue)
                }
                TextButton(onClick = { }) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Assesment2_MobproTheme {
        MainScreen()
    }
}
