package com.example.ecommerce.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.adapters.CartItemsAdapter
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.ui.HomeViewModel
import com.example.ecommerce.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking

class CartFragment : Fragment(R.layout.fragment_cart) {
    lateinit var viewModel: HomeViewModel
    lateinit var cartItemAdapter: CartItemsAdapter
    lateinit var binding: FragmentCartBinding
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        viewModel=( activity as MainActivity).viewModel
        setUpProductsRecyclerView()
        cartItemAdapter.setOnIncreaseClickedListener {p->
            runBlocking {
                if(p.quantity<=10) p.quantity++
                viewModel.saveCartItem(p)
                val total = binding.tvTotal.text.toString().toDouble()
                binding.tvTotal.text = (total + p.product.price!!).toString()
            }
        }
        cartItemAdapter.setOnDecreaseClickedListener {p->
            runBlocking {
                if(p.quantity>1) p.quantity--
                viewModel.saveCartItem(p)
                val total = binding.tvTotal.text.toString().toDouble()
                binding.tvTotal.text = (total - p.product.price!!).toString()
            }
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = cartItemAdapter.differ.currentList[position]
              runBlocking {
                  viewModel.deleteCartItem(article)

                Snackbar.make(view,"Successfully deleted article!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        runBlocking {
                            viewModel.saveCartItem(article)
                        }
                    }
                    show()
                }
              }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvCartItems)
        }
        viewModel.getAllCartItems().observe(viewLifecycleOwner,
            Observer { cartItems ->
                val data = cartItems
                var total:Double = 0.0
                data?.forEach { product ->
                    total += product.product.price?.times(product.quantity) ?: 0.0
                }
                binding.tvTotal.text = total.toString()
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