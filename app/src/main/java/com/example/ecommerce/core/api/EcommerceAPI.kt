package com.example.ecommerce.core.api

import com.example.ecommerce.core.models.categories_response.GetCategoriesResponse
import com.example.ecommerce.core.models.products_response.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EcommerceAPI {
    @GET("categories")
    suspend fun getAllCategories() : Response<GetCategoriesResponse>

    @GET("products")
    suspend fun getProductsByCategoryId(
        @Query("category_id") categoryId: Int
    ): Response<ProductsResponse>

    @GET("products/search")
    suspend fun searchInProducts(
        @Query("text") searchQuery: String
    ): Response<ProductsResponse>


}