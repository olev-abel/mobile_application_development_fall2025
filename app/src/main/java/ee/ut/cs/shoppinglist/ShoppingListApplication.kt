package ee.ut.cs.shoppinglist

import android.app.Application
import ee.ut.cs.shoppinglist.data.local.AppDatabaseProvider
import ee.ut.cs.shoppinglist.data.local.datastore.DataStoreViewModeRepository
import ee.ut.cs.shoppinglist.data.local.room.repository.RoomShoppingListRepository
import ee.ut.cs.shoppinglist.data.local.room.repository.RoomShoppingItemDetailsRepository

class ShoppingListApplication : Application() {
    val navCoordinator by lazy { NavCoordinator() }
    private val database by lazy { AppDatabaseProvider.getDatabase(applicationContext) }
    val shoppingListRepository by lazy {
        RoomShoppingListRepository(
            database.shoppingItemDao()
        )
    }
    val shoppingItemDetailsRepository by lazy {
        RoomShoppingItemDetailsRepository(
            database.shoppingItemDao()
        )
    }

    val viewModeRepository by lazy {
        DataStoreViewModeRepository(applicationContext)
    }
}