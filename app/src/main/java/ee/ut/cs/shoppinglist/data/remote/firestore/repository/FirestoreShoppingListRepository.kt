package ee.ut.cs.shoppinglist.data.remote.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.mapper.toDomain
import ee.ut.cs.shoppinglist.data.remote.firestore.mapper.toEntity
import ee.ut.cs.shoppinglist.data.remote.firestore.mapper.toFirestoreDto
import ee.ut.cs.shoppinglist.data.remote.firestore.model.ShoppingListFirestoreDto
import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreShoppingListRepository(
    private val firestoreDatabase: FirebaseFirestore,
    private val localDao: ShoppingItemDao
) : ShoppingListRepository {

    private val ITEMS_COLLECTION = "items"

    override fun observeItems(): Flow<List<ShoppingItem>> {
        return localDao.observeAll()
            .map { it.map { itemEntity -> itemEntity.toDomain() } }
    }

    override suspend fun upsert(item: ShoppingItem): NetworkResult<Unit> {
       return try {
            val docRef = firestoreDatabase.collection("items").document(item.id)
            docRef.set(item.toFirestoreDto()).await()
            refreshFromRemote()
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun delete(item: ShoppingItem): NetworkResult<Unit> {
       return try {
            firestoreDatabase.collection(ITEMS_COLLECTION).document(item.id).delete().await()
            refreshFromRemote()
        } catch (e: Exception) {
           return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun refreshFromRemote(): NetworkResult<Unit> {
        try {
            val res = firestoreDatabase.collection(ITEMS_COLLECTION).get().await()
            val entitites = res.documents.map { it.toObject(ShoppingListFirestoreDto::class.java) }
                .map { it?.toEntity() }
            localDao.replaceAll(entitites.filterNotNull())
            return NetworkResult.Success(Unit)
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: "Unknown error")
        }

    }

}