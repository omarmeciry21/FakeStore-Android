package com.example.ecommerce.core.models.products_response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("products")
data class Product(
    @PrimaryKey(true)
    var id:Int? = null,
    val description: String?,
    val discount: Int?,
    val image: String?,
    val in_cart: Boolean?,
    val in_favorites: Boolean?,
    val name: String?,
    val old_price: Double?,
    val price: Double?
)