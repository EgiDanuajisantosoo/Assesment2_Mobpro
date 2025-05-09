package com.egidanuajisantoso.assesment2_mobpro.ui.screen

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.egidanuajisantoso.assesment2_mobpro.R
import com.egidanuajisantoso.assesment2_mobpro.model.Barang
import com.egidanuajisantoso.assesment2_mobpro.navigation.Screen
import com.egidanuajisantoso.assesment2_mobpro.ui.theme.Assesment2_MobproTheme
import com.egidanuajisantoso.assesment2_mobpro.util.BarangViewModelFactory
import com.egidanuajisantoso.assesment2_mobpro.util.SettingDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: BarangViewModel = viewModel(
        factory = BarangViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val barangList by viewModel.allBarang.collectAsState(initial = emptyList())
    val dataStore = SettingDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)
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
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.RecycleBin.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = "Recycle Bin",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.FormBaru.route) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { padding ->
        ScreenContent(
            showList,
            modifier = Modifier.padding(padding),
            barangList = barangList,
            onEditClick = { barang ->
                navController.navigate(Screen.FormUbah.createRoute(barang.id))
            },
            onDeleteClick = { viewModel.softDeleteBarang(it) }
        )
    }
}

@Composable
fun ScreenContent(
    showList: Boolean,
    modifier: Modifier = Modifier,
    barangList: List<Barang>,
    onEditClick: (Barang) -> Unit,
    onDeleteClick: (Barang) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedBarang by remember { mutableStateOf<Barang?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (barangList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tidak ada data barang", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            if (showList) {
                LazyColumn(contentPadding = PaddingValues(4.dp)) {
                    items(barangList) { barang ->
                        BarangCard(
                            barang = barang,
                            onEditClick = { onEditClick(barang) },
                            onDeleteClick = {
                                selectedBarang = barang
                                showDialog = true
                            }
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(barangList.size) { index ->
                        val barang = barangList[index]
                        BarangCard(
                            barang = barang,
                            onEditClick = { onEditClick(barang) },
                            onDeleteClick = {
                                selectedBarang = barang
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog && selectedBarang != null) {
        DisplayAlertDialog(
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                onDeleteClick(selectedBarang!!)
                showDialog = false
            }
        )
    }
}



@Composable
fun BarangCard(
    barang: Barang,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Nama: ${barang.nama}",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Stok: ${barang.stok}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Harga: ${barang.harga}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onEditClick) {
                    Text("Edit", color = Color.Blue)
                }
                TextButton(onClick = onDeleteClick) {
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
        MainScreen(rememberNavController())
    }
}
