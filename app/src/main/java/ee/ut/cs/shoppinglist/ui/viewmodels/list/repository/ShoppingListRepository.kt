package ee.ut.cs.shoppinglist.ui.viewmodels.list.repository

import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeItems(): Flow<List<ShoppingItemEntity>>
    suspend fun upsert(item: ShoppingItemEntity)
    suspend fun delete(item: ShoppingItemEntity)


}