package com.example.ecommerce.core.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.core.models.CartItem
import com.example.ecommerce.databinding.ItemCartBinding

class CartItemsAdapter : RecyclerView.Adapter<CartItemsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val binding:ItemCartBinding ) : RecyclerView.ViewHolder(binding.root)
    private val differCallback = object : DiffUtil.ItemCallback<CartItem>(){
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var cartItem = differ.currentList[position]
        holder.binding.apply {
            Glide.with(this.root).load(cartItem.product.image).into(ivProductImage)
            tvProductName.text = cartItem.product.name
            tvProductPrice.text = cartItem.product.price.toString()
            ivIncreaseQuantity.setOnClickListener{
                onIncreaseClickedListener?.let { it1 -> it1(cartItem) }
                notifyItemChanged(position)
            }
            ivDecreaseQuantity.setOnClickListener{
                onDecreaseClickedListener?.let { it1 -> it1(cartItem) }
                notifyItemChanged(position)
            }
            tvProductQuantity.text = cartItem.quantity.toString()

        }
    }


    private var onIncreaseClickedListener : ((CartItem)->Unit)? = null

    fun setOnIncreaseClickedListener(listener: ((CartItem)->Unit) ) {
        onIncreaseClickedListener = listener
    }
    private var onDecreaseClickedListener : ((CartItem)->Unit)? = null

    fun setOnDecreaseClickedListener(listener: ((CartItem)->Unit) ) {
        onDecreaseClickedListener = listener
    }

}