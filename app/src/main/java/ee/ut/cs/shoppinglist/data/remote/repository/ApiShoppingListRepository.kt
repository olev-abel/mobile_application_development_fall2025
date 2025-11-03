package ee.ut.cs.shoppinglist.data.remote.repository

import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.mapper.toDomain
import ee.ut.cs.shoppinglist.data.local.room.mapper.toEntity
import ee.ut.cs.shoppinglist.data.remote.ApiService
import ee.ut.cs.shoppinglist.data.remote.mapper.toDTO
import ee.ut.cs.shoppinglist.data.remote.mapper.toEntity
import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class ApiShoppingListRepository(
    private val api: ApiService,
    private val localDao: ShoppingItemDao
) : ShoppingListRepository {

    override fun observeItems(): Flow<List<ShoppingItem>> =
        localDao.observeAll()
            .map { it.map { itemEntity -> itemEntity.toDomain() } }

    // Fetch remote list and replace local DB on success
    override suspend fun refreshFromRemote(): NetworkResult<Unit> {
        return when (val res = safeApiCall { api.getShoppingItems() }) {
            is NetworkResult.Success -> {
                // map DTOs -> domain ShoppingItem if needed; assuming body is List<ShoppingItem>
                val items = res.data
                localDao.replaceAll(items.map { it.toEntity() })
                NetworkResult.Success(Unit)
            }

            is NetworkResult.Error -> throw Exception("Failed to refresh: ${res.message} (code: ${res.code})")
            is NetworkResult.Loading -> NetworkResult.Error("Unexpected loading state")
        }
    }

    // Try network operation first; on success update local DB so flows emit updated data.
    // If network fails, optionally write to local for offline-first behavior (here we write only on success).
    override suspend fun upsert(item: ShoppingItem) {
        when (val res = safeApiCall { api.addShoppingItem(item.toDTO()) }) {
            is NetworkResult.Success -> {
                // server returned canonical item (with id, etc.)
                val serverItem = res.data
                localDao.upsert(serverItem.toEntity())
            }

            is NetworkResult.Error -> {
                // fallback: persist locally to not lose data, or rethrow/log
                localDao.upsert(item.toEntity())
            }

            is NetworkResult.Loading -> { /* no-op */
            }
        }
    }

    override suspend fun delete(item: ShoppingItem) {
        when (val res = safeApiCall { api.deleteShoppingItem(item.id) }) {
            is NetworkResult.Success -> localDao.delete(item.toEntity())
            is NetworkResult.Error -> {
                // fallback: remove locally or mark pending deletion; here remove locally
                localDao.delete(item.toEntity())
            }

            is NetworkResult.Loading -> { /* no-op */
            }
        }
    }

    // helper for Retrofit -> NetworkResult
    private suspend fun <T> safeApiCall(call: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) NetworkResult.Success(body)
                else NetworkResult.Error("Empty response body", response.code())
            } else {
                val errorText = parseError(response)
                NetworkResult.Error(errorText, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.localizedMessage ?: "Unknown network error")
        }
    }

    private fun <T> parseError(response: Response<T>): String {
        return try {
            response.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: response.message()
        } catch (_: Exception) {
            response.message()
        }
    }
}