package ee.ut.cs.shoppinglist.data.local.room.repository

import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class RoomShoppingListRepository(
    private val dao: ShoppingItemDao
) : ShoppingListRepository {
    // Implementation goes here
    override fun observeItems(): Flow<List<ShoppingItemEntity>> = dao.observeAll()

    override suspend fun upsert(item: ShoppingItemEntity)= dao.upsert(item)

    override suspend fun delete(item: ShoppingItemEntity) = dao.delete(item)
}