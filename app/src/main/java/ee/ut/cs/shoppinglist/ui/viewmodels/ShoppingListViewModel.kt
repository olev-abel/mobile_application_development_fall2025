package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.screens.ViewMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class ShoppingListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val navCoordinator: NavCoordinator
) : ViewModel() {

    private companion object {
        const val KEY_VIEW = "view_mode"
        const val KEY_SEARCH_QUERY = "search_query"
    }

    val query = savedStateHandle.getStateFlow(KEY_SEARCH_QUERY, "")
    val viewMode = savedStateHandle.getStateFlow(KEY_VIEW, ViewMode.All)

    fun toggleViewMode(mode: ViewMode) {
        savedStateHandle[KEY_VIEW] = mode
    }

    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items = _items.asStateFlow()

    val filteredItems = combine(query, _items) { queryString, combinedItems ->
        combinedItems.filter { it.name.contains(queryString) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _items.value)

    fun updateSearchQuery(newQuery: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = newQuery
    }

    fun saveNewItem(newItemUi: NewItemUi) {
        if (newItemUi.name.isBlank()
            || newItemUi.quantity.toIntOrNull() == null
        ) return
        addItem(
            ShoppingItem(
                name = newItemUi.name,
                quantity = newItemUi.quantity.toInt(),
                category = newItemUi.category, // or keep String
                image = null,
            )
        )
    }

    fun addItem(item: ShoppingItem) {
        _items.value = _items.value + item
    }

    fun removeItem(item: ShoppingItem) {
        _items.value = _items.value - item
    }

    fun updateItem(updated: ShoppingItem) {
        _items.value = _items.value.map { if (it.id == updated.id) updated else it }
    }

    fun toggleBought(item: ShoppingItem) {
        updateItem(item.copy(isBought = !item.isBought))
    }

    fun itemById(id: String): ShoppingItem {
        return _items.value.first { it.id == id }
    }

    fun openDetailScreen(id: String) {
        navCoordinator.toDetailScreen(id)
    }
}