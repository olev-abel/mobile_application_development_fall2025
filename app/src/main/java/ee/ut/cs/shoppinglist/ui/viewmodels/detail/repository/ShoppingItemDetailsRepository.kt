package ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository

import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingItemDetailsRepository {

    fun getById(id: String): Flow<ShoppingItemEntity?>
}