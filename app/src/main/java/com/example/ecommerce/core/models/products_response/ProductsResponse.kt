package com.example.ecommerce.core.models.products_response

data class ProductsResponse(
    val `data`: Data,
    val message: Any,
    val status: Boolean
)