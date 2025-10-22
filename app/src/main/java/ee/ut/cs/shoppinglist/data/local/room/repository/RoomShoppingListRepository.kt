package ee.ut.cs.shoppinglist.data.local.room.repository

import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.mapper.toDomain
import ee.ut.cs.shoppinglist.data.local.room.mapper.toEntity
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomShoppingListRepository(
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingListRepository {
    override fun observeItems(): Flow<List<ShoppingItem>> {
        return shoppingItemDao.observeAll().map({ it -> it.map { it.toDomain() } })
    }

    override suspend fun upsert(item: ShoppingItem) {
        shoppingItemDao.upsert(item.toEntity())
    }

    override suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.delete(item.toEntity())
    }

}