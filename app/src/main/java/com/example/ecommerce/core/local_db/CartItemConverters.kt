package com.example.ecommerce.core.local_db

import androidx.room.TypeConverter
import com.example.ecommerce.core.models.products_response.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartItemConverters {

    private val gson = Gson()

    // Convert Product to JSON string
    @TypeConverter
    fun fromProduct(product: Product?): String? {
        return product?.let { gson.toJson(it) }
    }

    // Convert JSON string back to Product
    @TypeConverter
    fun toProduct(json: String?): Product? {
        return json?.let {
            val productType = object : TypeToken<Product>() {}.type
            gson.fromJson(it, productType)
        }
    }
}