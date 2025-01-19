package com.example.ecommerce.core.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.models.products_response.Product

@Dao
interface ProductsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(product: Product)

    @Query("SELECT * FROM products")
    fun getAllProducts():LiveData<List<Product>>

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun checkIfFavorite(productId: Int): List<Product>
}