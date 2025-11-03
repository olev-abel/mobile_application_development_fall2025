package ee.ut.cs.shoppinglist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ee.ut.cs.shoppinglist.data.local.room.dao.ShoppingItemDao
import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity

@Database(entities = [ShoppingItemEntity::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDao
}