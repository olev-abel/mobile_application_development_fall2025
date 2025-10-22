package ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository

import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingItemDetailsRepository {
    fun getById(id: String) : Flow<ShoppingItem?>
}