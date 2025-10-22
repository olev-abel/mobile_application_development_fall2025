package ee.ut.cs.shoppinglist.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")
fun Context.appDataStore(): DataStore<Preferences> = this._dataStore