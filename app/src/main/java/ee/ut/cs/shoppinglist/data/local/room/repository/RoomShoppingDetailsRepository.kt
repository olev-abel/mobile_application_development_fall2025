package ee.ut.cs.shoppinglist.data.local.room.repository

import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.mapper.toDomain
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository.ShoppingItemDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomShoppingDetailsRepository(
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingItemDetailsRepository {
    override fun getById(id: String): Flow<ShoppingItem?> {
        return shoppingItemDao.getById(id).map({ it?.toDomain() })
    }
}