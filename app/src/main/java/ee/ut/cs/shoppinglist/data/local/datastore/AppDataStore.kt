package ee.ut.cs.shoppinglist.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Single, app-scoped Preferences DataStore
private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

// Public accessor
fun Context.appDataStore(): DataStore<Preferences> = this._dataStore