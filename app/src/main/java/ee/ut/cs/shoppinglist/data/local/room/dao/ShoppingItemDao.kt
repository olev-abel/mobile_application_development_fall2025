package ee.ut.cs.shoppinglist.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items ORDER BY name ASC")
    fun observeAll() : Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE id = :id LIMIT 1")
    fun getById(id: String): Flow<ShoppingItemEntity?>


    @Upsert
    suspend fun upsert(itemEntity: ShoppingItemEntity)

    @Delete
    suspend fun delete(itemEntity: ShoppingItemEntity)
}