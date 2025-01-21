package com.example.ecommerce.core.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.models.CartItem

@Dao
interface CartItemsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(product: CartItem) : Long

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems():LiveData<List<CartItem>>

    @Delete
    suspend fun deleteCartItem(product: CartItem) : Int

}