package com.example.ecommerce.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.adapters.CartItemsAdapter
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.ui.HomeViewModel
import com.example.ecommerce.ui.activities.MainActivity
import kotlinx.coroutines.runBlocking

class CartFragment : Fragment(R.layout.fragment_cart) {
    lateinit var viewModel: HomeViewModel
    lateinit var cartItemAdapter: CartItemsAdapter
    lateinit var binding: FragmentCartBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        viewModel=( activity as MainActivity).viewModel
        setUpProductsRecyclerView()
        cartItemAdapter.setOnIncreaseClickedListener {p->
            runBlocking {
                if(p.quantity<=10) p.quantity++
                viewModel.saveCartItem(p)
            }
        }
        cartItemAdapter.setOnDecreaseClickedListener {p->
            runBlocking {
                if(p.quantity>1) p.quantity--
                viewModel.saveCartItem(p)
            }
        }
        viewModel.getAllCartItems().observe(viewLifecycleOwner,
            Observer { cartItems ->
                val data = cartItems
                data?.forEach { product ->

                }
                cartItemAdapter.differ.submitList(data)

            })
    }

    private fun setUpProductsRecyclerView(){
        cartItemAdapter = CartItemsAdapter()
        binding.rvCartItems.apply {
            adapter = cartItemAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}