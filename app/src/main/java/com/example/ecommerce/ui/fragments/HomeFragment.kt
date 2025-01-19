package com.example.ecommerce.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.adapters.CategoriesAdapter
import com.example.ecommerce.core.adapters.ProductsAdapter
import com.example.ecommerce.core.models.CartItem
import com.example.ecommerce.core.models.categories_response.Category
import com.example.ecommerce.core.util.Resource
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.ui.HomeViewModel
import com.example.ecommerce.ui.activities.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel : HomeViewModel

    lateinit var categoriesAdapter: CategoriesAdapter

    lateinit var productsAdapter: ProductsAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        setUpCategoriesRecyclerView()
        setUpProductsRecyclerView()

        viewModel = (activity as MainActivity).viewModel
        productsAdapter.setOnFavouriteClickListener {p->
            runBlocking {
                if(viewModel.isFavorite(p)){
                    viewModel.deleteProduct(p)
                    Toast.makeText(context,"Product removed successfully!", Toast.LENGTH_LONG).show()
                }else{
                    viewModel.saveProduct(p)
                    Toast.makeText(context,"Product added successfully!", Toast.LENGTH_LONG).show()
                }
                val index = productsAdapter.differ.currentList.indexOf(p)
                productsAdapter.differ.currentList[index].isFavorite = viewModel.isFavorite(p)
                productsAdapter.notifyItemChanged(index)
            }
        }
        productsAdapter.setOnAddToCartClickListener {p->
            runBlocking {
                viewModel.saveCartItem(CartItem(
                    null,p,1
                ))
            }
        }
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
                    val data = response.data?.data?.data
                    data?.forEach { product ->
                        GlobalScope.launch(Dispatchers.IO) {
                            product.isFavorite = viewModel.isFavorite(product)
                        }
                    }
                    productsAdapter.differ.submitList(data)

                    binding.progressBar.visibility = View.GONE
                    if(response.data?.data?.data.isNullOrEmpty()){
                        binding.tvHomePlaceholder.visibility = View.VISIBLE
                        binding.tvHomePlaceholder.text = "No products found"
                    }
                }
                is Resource.Error ->{
                    response.message?.let { Log.e("HOMEACTIVITY", it) }
                    binding.progressBar.visibility = View.GONE
                    binding.tvHomePlaceholder.visibility = View.VISIBLE
                    binding.tvHomePlaceholder.text = response.message
                }
                is Resource.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvHomePlaceholder.visibility = View.GONE
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