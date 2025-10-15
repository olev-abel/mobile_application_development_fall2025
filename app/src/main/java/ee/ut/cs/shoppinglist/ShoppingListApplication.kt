package ee.ut.cs.shoppinglist

import android.app.Application

class ShoppingListApplication : Application() {
    val navCoordinator by lazy { NavCoordinator() }
}