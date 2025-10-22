package ee.ut.cs.shoppinglist

import android.app.Application
import ee.ut.cs.shoppinglist.data.local.AppDatabaseProvider
import ee.ut.cs.shoppinglist.data.local.datastore.DataStoreViewModeRepository
import ee.ut.cs.shoppinglist.data.local.room.repository.RoomShoppingDetailsRepository
import ee.ut.cs.shoppinglist.data.local.room.repository.RoomShoppingListRepository

class ShoppingListApplication : Application() {
    val navCoordinator by lazy { NavCoordinator() }

    val database by lazy { AppDatabaseProvider.getDatabase(applicationContext) }

    val shoppingListRepository by lazy { RoomShoppingListRepository(database.shoppingItemDao()) }
    val shoppingItemDetailsRepository by lazy {
        RoomShoppingDetailsRepository(database.shoppingItemDao())
    }
    val viewModeRepository by lazy {
        DataStoreViewModeRepository(applicationContext)
    }
}