package ee.ut.cs.shoppinglist

import android.app.Application
import ee.ut.cs.shoppinglist.data.local.AppDatabaseProvider
import ee.ut.cs.shoppinglist.data.local.datastore.DataStoreViewModeRepository
import ee.ut.cs.shoppinglist.data.local.room.repository.RoomShoppingDetailsRepository
import ee.ut.cs.shoppinglist.data.remote.RetrofitProvider
import ee.ut.cs.shoppinglist.data.remote.repository.ApiShoppingListRepository

class ShoppingListApplication : Application() {
    val navCoordinator by lazy { NavCoordinator() }

    val database by lazy { AppDatabaseProvider.getDatabase(applicationContext) }

    val shoppingListRepository by lazy {
        ApiShoppingListRepository(
            RetrofitProvider.api,
            database.shoppingItemDao()
        )
    }
    val shoppingItemDetailsRepository by lazy {
        RoomShoppingDetailsRepository(database.shoppingItemDao())
    }
    val viewModeRepository by lazy {
        DataStoreViewModeRepository(applicationContext)
    }
}