package com.example.ecommerce.core.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.core.models.products_response.Product
import com.example.ecommerce.databinding.ItemProductBinding

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val binding:ItemProductBinding ) : RecyclerView.ViewHolder(binding.root)
    private val differCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var product = differ.currentList[position]
        holder.binding.apply {
            Glide.with(this.root).load(product.image).into(imgProduct)
            tvProductName.text = product.name
            tvProductPrice.text = product.price.toString()
        }
    }

    private var onItemClickListener : ((Product)->Unit)? = null

    fun setOnItemClickListener(listener: ((Product)->Unit) ) {
        onItemClickListener = listener
    }

}