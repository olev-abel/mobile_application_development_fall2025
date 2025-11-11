package ee.ut.cs.shoppinglist

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ee.ut.cs.shoppinglist.common.AndroidResourceProvider
import ee.ut.cs.shoppinglist.data.local.AppDatabaseProvider
import ee.ut.cs.shoppinglist.data.local.datastore.DataStoreViewModeRepository
import ee.ut.cs.shoppinglist.data.local.room.repository.RoomShoppingDetailsRepository
import ee.ut.cs.shoppinglist.data.remote.RetrofitProvider
import ee.ut.cs.shoppinglist.data.remote.firestore.repository.FirestoreShoppingListRepository
import ee.ut.cs.shoppinglist.data.remote.repository.ApiShoppingListRepository
import ee.ut.cs.shoppinglist.domain.authentication.AuthenticationRepositoryImpl

class ShoppingListApplication : Application() {
    val navCoordinator by lazy { NavCoordinator() }

    private val database by lazy { AppDatabaseProvider.getDatabase(applicationContext) }

    val shoppingListRepository by lazy {
        FirestoreShoppingListRepository(
            firestoreDatabase = firestoreDatabase,
            localDao = database.shoppingItemDao()
        )
    }
    val shoppingItemDetailsRepository by lazy {
        RoomShoppingDetailsRepository(database.shoppingItemDao())
    }
    val viewModeRepository by lazy {
        DataStoreViewModeRepository(applicationContext)
    }

    val authenticationRepository by lazy { AuthenticationRepositoryImpl(applicationContext, firebaseAuth) }

    val resourceProvider by lazy { AndroidResourceProvider(applicationContext) }
    private val firestoreDatabase by lazy {
        FirebaseApp.initializeApp(this)
        Firebase.firestore
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}