package ee.ut.cs.shoppinglist.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import ee.ut.cs.shoppinglist.ui.screens.ViewMode
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ViewModeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val VIEW_MODE_KEY = stringPreferencesKey("view_mode")

class DataStoreViewModeRepository(private val context: Context) : ViewModeRepository {


    // Persist a new ViewMode
    override suspend fun updateViewMode(mode: ViewMode) {
        context.appDataStore().edit { prefs ->
            prefs[VIEW_MODE_KEY] = mode.name
        }
    }

    override fun observeViewMode(): Flow<ViewMode> {
       return context.appDataStore().data
            .map { prefs ->
                prefs[VIEW_MODE_KEY]?.let { name ->
                    try {
                        ViewMode.valueOf(name)
                    } catch (e: IllegalArgumentException) {
                        ViewMode.All
                    }
                } ?: ViewMode.All
            }
    }
}