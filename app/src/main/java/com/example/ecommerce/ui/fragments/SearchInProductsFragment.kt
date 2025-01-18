package com.example.ecommerce.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentSearchInProductsBinding

class SearchInProductsFragment : Fragment(R.layout.fragment_search_in_products) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchInProductsBinding.bind(view)
    }
}