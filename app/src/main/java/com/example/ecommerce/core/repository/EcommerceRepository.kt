package com.example.ecommerce.core.repository

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.api.RetrofitInstance
import com.example.ecommerce.core.local_db.CartItemDatabase
import com.example.ecommerce.core.local_db.ProductsDatabase
import com.example.ecommerce.core.models.CartItem
import com.example.ecommerce.core.models.products_response.Product

class EcommerceRepository(val productsDatabase: ProductsDatabase,val cartItemsDatabase : CartItemDatabase) {
    suspend fun getAllCategories() = RetrofitInstance.api.getAllCategories()
    suspend fun getProductsByCategoryId(categoryId:Int) = RetrofitInstance.api.getProductsByCategoryId(categoryId)
    suspend fun searchInProducts(searchQuery:String) = RetrofitInstance.api.searchInProducts(searchQuery)

    suspend fun saveProduct(product: Product) = productsDatabase.getProductDao().updateOrInsert(product)
    fun getAllProducts()=productsDatabase.getProductDao().getAllProducts()
    suspend fun deleteProduct(product: Product) = productsDatabase.getProductDao().deleteProduct(product)
    suspend fun isFavorite(product: Product) = productsDatabase.getProductDao().checkIfFavorite(product.id?:-1).size>0


    suspend fun updateOrInsert(product: CartItem) :Long= cartItemsDatabase.getProductDao().updateOrInsert(product)
    fun getAllCartItems(): LiveData<List<CartItem>> = cartItemsDatabase.getProductDao().getAllCartItems()
    suspend fun deleteCartItem(product: CartItem) :Int= cartItemsDatabase.getProductDao().deleteCartItem(product)
}