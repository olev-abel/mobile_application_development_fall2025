package ee.ut.cs.shoppinglist.data.local.room.repository

import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.mapper.toDomain
import ee.ut.cs.shoppinglist.data.local.room.mapper.toEntity
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomShoppingListRepository(
    private val dao: ShoppingItemDao
) : ShoppingListRepository {
    override fun observeItems(): Flow<List<ShoppingItem>> {
        return dao.observeAll().map({ it -> it.map { entity -> entity.toDomain() } })
    }

    override suspend fun upsert(item: ShoppingItem) = dao.upsert(item.toEntity())


    override suspend fun delete(item: ShoppingItem) = dao.delete(item.toEntity())
}