package com.example.ecommerce.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.InvalidationTracker
import com.example.ecommerce.R
import com.example.ecommerce.core.adapters.ProductsAdapter
import com.example.ecommerce.databinding.FragmentFavouriteProductsBinding
import com.example.ecommerce.ui.HomeViewModel
import com.example.ecommerce.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class FavouriteProductsFragment : Fragment(R.layout.fragment_favourite_products) {
    lateinit var viewModel: HomeViewModel
    lateinit var productsAdapter: ProductsAdapter
    lateinit var binding: FragmentFavouriteProductsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouriteProductsBinding.bind(view)
        viewModel=( activity as MainActivity).viewModel
        setUpProductsRecyclerView()
        productsAdapter.setOnFavouriteClickListener {p->
            runBlocking {
                if(viewModel.isFavorite(p)){
                    viewModel.deleteProduct(p)
                    Toast.makeText(context,"Product removed successfully!", Toast.LENGTH_LONG).show()
                }else{
                    viewModel.saveProduct(p)
                    Toast.makeText(context,"Product added successfully!", Toast.LENGTH_LONG).show()
                }
                viewModel.getSavedProducts()
            }
        }
        viewModel.getSavedProducts().observe(viewLifecycleOwner,
            Observer { products ->
                val data = products
                data?.forEach { product ->
                    GlobalScope.launch(Dispatchers.IO) {
                        product.isFavorite = viewModel.isFavorite(product)
                    }
                }
                productsAdapter.differ.submitList(data)

            })
    }

    private fun setUpProductsRecyclerView(){
        productsAdapter = ProductsAdapter()
        binding.rvSaved.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}