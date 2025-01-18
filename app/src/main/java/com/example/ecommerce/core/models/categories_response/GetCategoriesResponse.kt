package com.example.ecommerce.core.models.categories_response

data class GetCategoriesResponse(
    val `data`: Data,
    val message: Any,
    val status: Boolean
)