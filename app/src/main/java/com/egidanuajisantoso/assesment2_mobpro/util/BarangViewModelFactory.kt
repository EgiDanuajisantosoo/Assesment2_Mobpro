package com.egidanuajisantoso.assesment2_mobpro.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.egidanuajisantoso.assesment2_mobpro.ui.screen.BarangViewModel

class BarangViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarangViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarangViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}