package ee.ut.cs.shoppinglist.data.remote

import ee.ut.cs.shoppinglist.data.remote.model.ShoppingItemDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/items")
    suspend fun getShoppingItems(): Response<List<ShoppingItemDto>>

    @POST("/items")
    suspend fun addShoppingItem(@Body item: ShoppingItemDto): Response<ShoppingItemDto>

    @DELETE("/items/{id}")
    suspend fun deleteShoppingItem(@Path("id") id: String): Response<ShoppingItemDto>
}