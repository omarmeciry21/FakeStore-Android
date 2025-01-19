package com.example.ecommerce.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ecommerce.core.models.products_response.Product

@Entity("cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val product: Product,
    var quantity: Int


    )