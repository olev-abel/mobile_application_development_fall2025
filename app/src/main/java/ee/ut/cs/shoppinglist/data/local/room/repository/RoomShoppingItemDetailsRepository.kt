package ee.ut.cs.shoppinglist.data.local.room.repository

import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository.ShoppingItemDetailsRepository
import kotlinx.coroutines.flow.Flow

class RoomShoppingItemDetailsRepository(
    private val dao: ShoppingItemDao
) : ShoppingItemDetailsRepository {
    override fun getById(id: String): Flow<ShoppingItemEntity?> = dao.getById(id)
}