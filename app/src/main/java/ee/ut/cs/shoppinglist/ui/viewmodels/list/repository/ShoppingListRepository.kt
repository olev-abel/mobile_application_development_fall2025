package ee.ut.cs.shoppinglist.ui.viewmodels.list.repository

import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeItems(): Flow<List<ShoppingItem>>
    suspend fun upsert(item: ShoppingItem): NetworkResult<Unit>
    suspend fun delete(item: ShoppingItem): NetworkResult<Unit>

    suspend fun refreshFromRemote(): NetworkResult<Unit>
}