package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.screens.ViewMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow

import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.collections.filter


sealed interface NavEvent {
    data class ToItemDetail(val id: String) : NavEvent
    data object Back : NavEvent
}

class ShoppingListViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

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


    val filteredItems = combine(flow = query, flow2 = _items) { queryString, combinedItems ->
        combinedItems.filter { it.name.contains(queryString) }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(), _items.value
    )

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

    fun updateSearchQuery(newQuery: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = newQuery
    }

    fun addItem(item: ShoppingItem) {
        _items.update {
            it + item
        }
    }

    fun removeItem(item: ShoppingItem) {
        _items.update {
            it - item
        }
    }

    fun updateItem(updated: ShoppingItem) {
        _items.update { items ->
            items.map { if (it.id == updated.id) updated else it }
        }
    }

    fun toggleBought(item: ShoppingItem) {
        updateItem(item.copy(isBought = !item.isBought))
    }

    fun itemById(id: String): ShoppingItem {
        return items.value.first { it.id == id }
    }

    // BUFFERED so a quick event won’t get dropped if collector isn’t ready that instant
    private val _navChannel = Channel<NavEvent>(capacity = Channel.BUFFERED)

    // Expose as Flow so UI collects it in a lifecycle-aware way
    val navEvents = _navChannel.receiveAsFlow()

    fun openItemDetail(id: String) {
        // non-suspending send; use trySend so UI thread isn’t blocked
        _navChannel.trySend(NavEvent.ToItemDetail(id))
    }

    fun navigateBack() {
        _navChannel.trySend(NavEvent.Back)
    }

    override fun onCleared() {
        _navChannel.close() // optional; helps fail-fast if used after clear
        super.onCleared()
    }

}