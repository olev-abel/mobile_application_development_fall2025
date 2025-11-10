package ee.ut.cs.shoppinglist

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
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

    override fun onCreate() {
        super.onCreate()
        try {
            val app = FirebaseApp.initializeApp(this)
            if (app == null) {
                Log.e("FirebaseInit", "FirebaseApp.initializeApp returned null. Check google-services.json and applicationId.")
            } else {
                Log.d("FirebaseInit", "Firebase initialized: ${app.name}")
            }
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Firebase initialization failed", e)
        }
    }
}