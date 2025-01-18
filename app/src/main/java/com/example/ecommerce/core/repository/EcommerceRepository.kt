package com.example.ecommerce.core.repository

import com.example.ecommerce.core.api.RetrofitInstance
import com.example.ecommerce.core.models.categories_response.Category

class EcommerceRepository {
    suspend fun getAllCategories() = RetrofitInstance.api.getAllCategories()
    suspend fun getProductsByCategoryId(categoryId:Int) = RetrofitInstance.api.getProductsByCategoryId(categoryId)
    suspend fun searchInProducts(searchQuery:String) = RetrofitInstance.api.searchInProducts(searchQuery)
}