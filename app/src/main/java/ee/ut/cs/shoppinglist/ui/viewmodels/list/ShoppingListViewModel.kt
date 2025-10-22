package ee.ut.cs.shoppinglist.ui.viewmodels.list


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.screens.ViewMode
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ViewModeRepository

import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ShoppingListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val navCoordinator: NavCoordinator,
    private val listRepository: ShoppingListRepository,
    private val viewModeRepository: ViewModeRepository
) : ViewModel() {

    private companion object {
        const val KEY_VIEW = "view_mode"
        const val KEY_SEARCH_QUERY = "search_query"
    }

    val query = savedStateHandle.getStateFlow(KEY_SEARCH_QUERY, "")
    val viewMode = viewModeRepository.observeViewMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ViewMode.All)

    fun toggleViewMode(mode: ViewMode) {
        viewModelScope.launch {
            viewModeRepository.updateViewMode(mode)
        }
    }

    private val _items = listRepository.observeItems()
    val items = _items.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val filteredItems = combine(query, _items) { queryString, combinedItems ->
        combinedItems.filter { it.name.contains(queryString) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
        viewModelScope.launch {
            listRepository.upsert(item)
        }
    }

    fun removeItem(item: ShoppingItem) {
        viewModelScope.launch {
            listRepository.delete(item)
        }
    }

    fun toggleBought(item: ShoppingItem) {
        val updatedItem = item.copy(isBought = !item.isBought)
        viewModelScope.launch {
            listRepository.upsert(updatedItem)
        }
    }

    fun openDetailScreen(id: String) {
        navCoordinator.toDetailScreen(id)
    }
}