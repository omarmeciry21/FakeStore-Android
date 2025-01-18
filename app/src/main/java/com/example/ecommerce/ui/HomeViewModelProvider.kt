package com.example.ecommerce.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerce.core.repository.EcommerceRepository

class HomeViewModelProvider(
    private val app: Application,
    private val ecommerceRepository: EcommerceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(app, ecommerceRepository) as T
    }
}