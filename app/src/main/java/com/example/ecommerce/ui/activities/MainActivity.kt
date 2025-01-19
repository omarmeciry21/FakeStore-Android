package com.example.ecommerce.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.R
import com.example.ecommerce.core.local_db.ProductsDatabase
import com.example.ecommerce.core.repository.EcommerceRepository
import com.example.ecommerce.databinding.ActivityMainBinding
import com.example.ecommerce.ui.HomeViewModel
import com.example.ecommerce.ui.HomeViewModelProvider

class MainActivity : AppCompatActivity() {
    lateinit var viewModel : HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()


        viewModel = ViewModelProvider(
            this,
            HomeViewModelProvider(application, EcommerceRepository(ProductsDatabase(this)))
        ).get(HomeViewModel::class.java)

        val newsNavHostFragment= supportFragmentManager.findFragmentById(R.id.ecommerceMainFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}