package ee.ut.cs.shoppinglist.ui.viewmodels.list.repository

import ee.ut.cs.shoppinglist.ui.screens.ViewMode
import kotlinx.coroutines.flow.Flow

interface ViewModeRepository {
    fun observeViewMode(): Flow<ViewMode>
    suspend fun updateViewMode(mode: ViewMode)
}