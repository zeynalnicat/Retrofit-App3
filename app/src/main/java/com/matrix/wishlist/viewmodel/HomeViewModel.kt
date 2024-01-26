package com.matrix.wishlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrix.wishlist.api.ProductApi
import com.matrix.wishlist.api.RetrofitInstance
import com.matrix.wishlist.db.RoomDb
import com.matrix.wishlist.db.category.CategoryEntity
import com.matrix.wishlist.db.product.ProductEntity

import com.matrix.wishlist.model.ProductList
import com.matrix.wishlist.model.ProductRoomModel
import com.matrix.wishlist.resource.Resource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class HomeViewModel(private val roomDb: RoomDb) : ViewModel() {
    private val _products = MutableLiveData<Resource<List<ProductRoomModel>>>()
    private val productApi = RetrofitInstance.getRetrofitInstance()?.create(ProductApi::class.java)


    private val _roomSize = MutableLiveData<Int>(0)
    val products: LiveData<Resource<List<ProductRoomModel>>>
        get() = _products


    private val _categories = MutableLiveData<Resource<List<String>>>()
    val categories: LiveData<Resource<List<String>>>
        get() = _categories

    val roomSize: LiveData<Int>
        get() = _roomSize

    val isRoom = MutableLiveData<Boolean>(false)

    fun getProducts() {
        _products.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = productApi?.getProducts()
                if (response!!.isSuccessful) {
                    val productList = response.body()?.products?.map {
                        ProductRoomModel(
                            id = it.id,
                            brand = it.brand,
                            description = it.description,
                            title = it.title,
                            price = it.price,
                            stock = it.stock,
                            thumbnail = it.thumbnail,
                            images = it.images[0],
                            category = it.category,
                            discountPercentage = it.discountPercentage,
                            rating = it.rating
                        )
                    }
                    productList?.let {
                        _products.postValue(Resource.Success(it))
                    }


                } else {
                    _products.postValue(Resource.Error(Exception("There was an error while handling the request")))
                }
            } catch (e: Exception) {
                _products.postValue(Resource.Error(e))

            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = productApi?.getCategories()
                response?.let {
                    if (it.isSuccessful) {
                        isRoom.postValue(false)
                        response.body()?.let { listString ->
                            val tempList = listString.toMutableList()
                            tempList.add(0, "All")
                            _categories.postValue(Resource.Success(listString))

                        }
                    } else {
                        _categories.postValue(Resource.Error(Exception("There was an error while handling the request")))

                    }
                }


            } catch (e: Exception) {
                _categories.postValue(Resource.Error(e))
            }
        }
    }

    fun clearProducts() {
        _products.postValue(Resource.Loading)
        _products.postValue(Resource.Success(emptyList()))
    }

    fun clearCategories() {
        _categories.postValue(Resource.Loading)
        _categories.postValue(Resource.Success(emptyList()))
    }

    suspend fun search(query: String): Response<ProductList> {
        return productApi!!.search(query)
    }

    fun getDueCategory(category: String) {
        _products.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = productApi?.getSearchedCategory(category)
                response?.let {
                    if (it.isSuccessful) {
                        val productList = response.body()?.products?.map { product ->
                            ProductRoomModel(
                                id = product.id,
                                brand = product.brand,
                                description = product.description,
                                title = product.title,
                                price = product.price,
                                stock = product.stock,
                                thumbnail = product.thumbnail,
                                images = product.images[0],
                                category = product.category,
                                discountPercentage = product.discountPercentage,
                                rating = product.rating
                            )
                        }
                        productList?.let { list ->
                            _products.postValue(Resource.Success(list))
                        }
                    } else {
                        _products.postValue(Resource.Error(Exception("There was an error while handling the request")))
                    }
                }

            } catch (e: Exception) {
                _products.postValue(Resource.Error(e))
            }

        }
    }

    fun updateProducts(query: String) {
        _products.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = search(query)
                if (products.isSuccessful) {
                    val productList = products.body()?.products?.map {
                        ProductRoomModel(
                            id = it.id,
                            brand = it.brand,
                            description = it.description,
                            title = it.title,
                            price = it.price,
                            stock = it.stock,
                            thumbnail = it.thumbnail,
                            images = it.images[0],
                            category = it.category,
                            discountPercentage = it.discountPercentage,
                            rating = it.rating
                        )
                    }
                    productList?.let {
                        _products.postValue(Resource.Success(it))
                    }
                } else {
                    _products.postValue(Resource.Error(Exception("There was an error while handling the request")))
                    getFromDB()
                }

            } catch (e: Exception) {
                _products.postValue(Resource.Error(e))
                getFromDB()
            }
        }

    }

    fun insertDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val productList = _products.value
            if (productList is Resource.Success) {
                val productDao = roomDb.productDao()
                productList.data.forEach {
                    productDao.insert(
                        ProductEntity(
                            id = it.id,
                            brand = it.brand,
                            description = it.description,
                            title = it.title,
                            price = it.price,
                            stock = it.stock,
                            thumbnail = it.thumbnail,
                            images = it.images,
                            category = it.category,
                            discountPercentage = it.discountPercentage,
                            rating = it.rating
                        )
                    )
                }
            }

        }
    }

    fun getFromDB() {
        _products.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val productDao = roomDb.productDao()
            val products = productDao.getAll()
            if (products.isNotEmpty()) {
                val productList = products.map {
                    ProductRoomModel(
                        id = it.id,
                        brand = it.brand,
                        description = it.description,
                        title = it.title,
                        price = it.price,
                        stock = it.stock,
                        thumbnail = it.thumbnail,
                        images = it.images,
                        category = it.category,
                        discountPercentage = it.discountPercentage,
                        rating = it.rating
                    )
                }
                _products.postValue(Resource.Success(productList))
                _roomSize.postValue(products.size)
            }
        }
    }

    fun checkDb(){
        val productDao = roomDb.productDao()
        viewModelScope.launch {
            val size = productDao.checkDb()
            _roomSize.postValue(size)
        }
    }

    fun insertCategoryDB() {
        val categoryDao = roomDb.categoryDao()
        val categoryList = _categories.value
        if (categoryList is Resource.Success) {
            viewModelScope.launch(Dispatchers.Default) {
                categoryList.data.forEach {
                    categoryDao.insert(CategoryEntity(name = it))
                }
            }
        }

    }

    fun getDbCategories() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoryDao = roomDb.categoryDao()
                val categoryList = categoryDao.getCategories()

                if (categoryList.isNotEmpty()) {
                    isRoom.postValue(true)
                    val tempList = categoryList.toMutableList()
                    tempList.add(0, "All")
                    val temp2 = tempList.toSet()
                    _categories.postValue(Resource.Success(temp2.toList()))
                } else {
                    _categories.postValue(Resource.Error(Exception("There was an error while handling the request")))
                }

            } catch (e: Exception) {
                _categories.postValue(Resource.Error(e))
            }

        }
    }

    fun getDbDueCategories(category: String) {
        val productList = _products.value
        if (productList is Resource.Success) {
            if (category == "All") {
                getFromDB()
            } else {
                _products.postValue(Resource.Success(productList.data.filter { it.category == category }))
            }
        }
    }

    fun searchRoom(string: String) {
        if (string.isEmpty()) {
            getFromDB()
        } else {
            try {
                _products.postValue(Resource.Loading)
                viewModelScope.launch(Dispatchers.IO) {
                    val productList = roomDb.productDao().getSearched(string)
                    if (productList.isNotEmpty()) {
                        val productRoomModel = productList.map {
                            ProductRoomModel(
                                id = it.id,
                                brand = it.brand,
                                description = it.description,
                                title = it.title,
                                price = it.price,
                                stock = it.stock,
                                thumbnail = it.thumbnail,
                                images = it.images,
                                category = it.category,
                                discountPercentage = it.discountPercentage,
                                rating = it.rating
                            )
                        }
                        _products.postValue(Resource.Success(productRoomModel))
                    }
                }
            } catch (e: Exception) {
                _products.postValue(Resource.Error(e))

            }
        }
    }

    fun sort(sorting: String, isRoom: Boolean) {
        val productList = _products.value
        if (productList is Resource.Success) {

            when (sorting) {
                "Default" -> {
                    if (!isRoom) {
                        getProducts()
                    } else {
                        getFromDB()
                    }
                }

                "A-Z" -> _products.postValue(Resource.Success(productList.data.sortedBy { it.title }))
                "Z-A" -> _products.postValue(Resource.Success(productList.data.sortedByDescending { it.title }))
                "ASC-Price" -> _products.postValue(Resource.Success(productList.data.sortedBy { it.price }))
                "DESC-Price" -> _products.postValue(Resource.Success(productList.data.sortedByDescending { it.price }))
            }
        }
    }




}