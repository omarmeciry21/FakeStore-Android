package com.example.ecommerce.core.repository

import com.example.ecommerce.core.api.RetrofitInstance
import com.example.ecommerce.core.local_db.ProductsDatabase
import com.example.ecommerce.core.models.products_response.Product

class EcommerceRepository(val productsDatabase: ProductsDatabase) {
    suspend fun getAllCategories() = RetrofitInstance.api.getAllCategories()
    suspend fun getProductsByCategoryId(categoryId:Int) = RetrofitInstance.api.getProductsByCategoryId(categoryId)
    suspend fun searchInProducts(searchQuery:String) = RetrofitInstance.api.searchInProducts(searchQuery)

    suspend fun saveProduct(product: Product) = productsDatabase.getProductDao().updateOrInsert(product)
    fun getAllProducts()=productsDatabase.getProductDao().getAllProducts()
    suspend fun deleteProduct(product: Product) = productsDatabase.getProductDao().deleteProduct(product)
    suspend fun isFavorite(product: Product) = productsDatabase.getProductDao().checkIfFavorite(product.id?:-1).size>0
}