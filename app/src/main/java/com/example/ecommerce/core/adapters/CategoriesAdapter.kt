package com.example.ecommerce.core.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.models.categories_response.Category
import com.example.ecommerce.databinding.ItemCategoryBinding

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val binding:ItemCategoryBinding ) : RecyclerView.ViewHolder(binding.root)
    private val differCallback = object : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater,parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var category = differ.currentList[position]
        holder.binding.apply {
            tvCategory.text = category.name
            tvCategoryBg.setOnClickListener{
                onItemClickListener?.let {
                    it(category)
                }
            }
            if(selectedIndex== position){
                @ColorInt
                val Primary100 = 0xffE6E6E6
                tvCategory.setTextColor(ColorStateList.valueOf(Primary100.toInt()))
                tvCategoryBg.setBackgroundResource(R.drawable.active_category_item_background)
            } else {
                tvCategory.setTextColor(ColorStateList.valueOf(Color.BLACK))
                tvCategoryBg.setBackgroundResource(R.drawable.inactive_category_item_background)
            }
        }
    }

    var selectedIndex = 0

    fun updateSelectedIndex(index: Int){
        selectedIndex = index
        notifyDataSetChanged()
    }

    private var onItemClickListener : ((Category)->Unit)? = null

    fun setOnItemClickListener(listener: ((Category)->Unit) ) {
        onItemClickListener = listener
    }

}