package com.example.ecommerce.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.models.categories_response.GetCategoriesResponse
import com.example.ecommerce.core.models.products_response.Product
import com.example.ecommerce.core.models.products_response.ProductsResponse
import com.example.ecommerce.core.repository.EcommerceRepository
import com.example.ecommerce.core.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(val app: Application, val ecommerceRepository: EcommerceRepository): AndroidViewModel(app) {

    val categoriesResponse: MutableLiveData<Resource<GetCategoriesResponse>> = MutableLiveData()
    val productsResponse: MutableLiveData<Resource<ProductsResponse>> = MutableLiveData()
    val searchInProductsResponse: MutableLiveData<Resource<ProductsResponse>> = MutableLiveData()

    init {
        getAllCategories()
    }

    fun getAllCategories(){
        viewModelScope.launch {
            categoriesResponse.postValue(Resource.Loading())
            val response = ecommerceRepository.getAllCategories()
            categoriesResponse.postValue(handleCategoriesResponse(response))
        }
    }

    private fun handleCategoriesResponse(response: Response<GetCategoriesResponse>): Resource<GetCategoriesResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getProductsByCategoryId( id: Int){
        viewModelScope.launch {
            productsResponse.postValue(Resource.Loading())
            val response = ecommerceRepository.getProductsByCategoryId(id)
            productsResponse.postValue(handleProductsResponse(response))
        }
    }

    fun searchInProducts( searchQuery: String){
        viewModelScope.launch {
            searchInProductsResponse.postValue(Resource.Loading())
            val response = ecommerceRepository.searchInProducts(searchQuery)
            searchInProductsResponse.postValue(handleProductsResponse(response))
        }
    }

    private fun handleProductsResponse(response: Response<ProductsResponse>): Resource<ProductsResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveProduct(product: Product){viewModelScope.launch {
        ecommerceRepository.saveProduct(product)
    }}
    
    suspend fun isFavorite(product: Product) : Boolean{
        return ecommerceRepository.isFavorite(product)
    }

    fun getSavedProducts() = ecommerceRepository.getAllProducts()

    fun deleteProduct(product: Product){viewModelScope.launch {
        ecommerceRepository.deleteProduct(product)
    }}
}