package com.example.ecommerce.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.adapters.CategoriesAdapter
import com.example.ecommerce.core.adapters.ProductsAdapter
import com.example.ecommerce.core.models.categories_response.Category
import com.example.ecommerce.core.repository.EcommerceRepository
import com.example.ecommerce.core.util.Resource
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.ui.HomeViewModel
import com.example.ecommerce.ui.HomeViewModelProvider
import com.example.ecommerce.ui.activities.MainActivity

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel : HomeViewModel

    lateinit var categoriesAdapter: CategoriesAdapter

    lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        setUpCategoriesRecyclerView()
        setUpProductsRecyclerView()

        viewModel = (activity as MainActivity).viewModel

        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success ->{
                    categoriesAdapter.differ.submitList(response.data?.data?.data)
                    val categories = response.data?.data?.data
                    if(categories?.isNotEmpty() == true){
                        categoriesAdapter.updateSelectedIndex(0)
                        viewModel.getProductsByCategoryId(categories[0].id)
                    }
                    categoriesAdapter.setOnItemClickListener { category ->
                        categories?.indexOf(category)
                            ?.let {
                                categoriesAdapter.updateSelectedIndex(it)
                                viewModel.getProductsByCategoryId(categories[it].id)
                            }

                    }
                }
                is Resource.Error ->{
                    response.message?.let { Log.e("HOMEACTIVITY", it) }
                }
                is Resource.Loading ->{

                }
            }
        })

        viewModel.productsResponse.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success ->{
                    productsAdapter.differ.submitList(response.data?.data?.data)
                }
                is Resource.Error ->{
                    response.message?.let { Log.e("HOMEACTIVITY", it) }
                }
                is Resource.Loading ->{

                }
            }
        })
    }

    fun onCategorySelected(category: Category):Unit{}

    private fun setUpProductsRecyclerView(){
        productsAdapter = ProductsAdapter()
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun setUpCategoriesRecyclerView(){
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            adapter = categoriesAdapter
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}