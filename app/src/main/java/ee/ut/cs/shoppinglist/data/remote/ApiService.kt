package ee.ut.cs.shoppinglist.data.remote

import ee.ut.cs.shoppinglist.data.remote.model.ShoppingItemDto

interface ApiService {

    suspend fun getShoppingItems(): List<ShoppingItemDto>
}