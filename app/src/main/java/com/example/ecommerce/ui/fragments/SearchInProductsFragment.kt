package com.example.ecommerce.ui.fragments

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.adapters.ProductsAdapter
import com.example.ecommerce.core.util.Resource
import com.example.ecommerce.databinding.FragmentSearchInProductsBinding
import com.example.ecommerce.ui.HomeViewModel
import com.example.ecommerce.ui.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchInProductsFragment : Fragment(R.layout.fragment_search_in_products) {
    lateinit var binding: FragmentSearchInProductsBinding
    lateinit var viewModel : HomeViewModel
    lateinit var productsAdapter: ProductsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchInProductsBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel

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
                val index = productsAdapter.differ.currentList.indexOf(p)
                productsAdapter.differ.currentList[index].isFavorite = viewModel.isFavorite(p)
                productsAdapter.notifyItemChanged(index)
            }
        }
        var job: Job? =null
        binding.etSearch.addTextChangedListener{editable->
            job?.cancel()
            job = MainScope().launch {
                editable?.let{
                    viewModel.searchInProducts(editable.toString())
                }
            }
        }

        viewModel.searchInProductsResponse.observe(viewLifecycleOwner, Observer {response->
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

    private fun setUpProductsRecyclerView(){
        productsAdapter = ProductsAdapter()
        binding.rvProducts.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}