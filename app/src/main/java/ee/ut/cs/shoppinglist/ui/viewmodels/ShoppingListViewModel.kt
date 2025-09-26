package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.screens.ViewMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NewItemUi(
    val name: String = "This is item",
    val quantity: String = "1",
    val category: ShoppingCategory = ShoppingCategory.MISC
)


class ShoppingListViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private companion object {
        const val KEY_VIEW = "view_mode"
    }

    var viewMode by mutableStateOf(savedStateHandle[KEY_VIEW] ?: ViewMode.All)
        private set

    fun toggleViewMode(mode: ViewMode) {
        viewMode = mode
        savedStateHandle[KEY_VIEW] = mode
    }

    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items = _items.asStateFlow()

    var showAddDialog by mutableStateOf(false)
    var newItem by mutableStateOf(NewItemUi())
    var categories by mutableStateOf(ShoppingCategory.entries.toTypedArray())

    fun openAdd() {
        showAddDialog = true; newItem = NewItemUi()
    }

    fun closeAdd() {
        showAddDialog = false
    }

    fun saveNewItem() {
        if (newItem.name.isBlank()
            || newItem.quantity.isBlank()
            || newItem.quantity.toIntOrNull() == null) return
        addItem(
            ShoppingItem(
                name = newItem.name,
                quantity = newItem.quantity.toInt(),
                category = newItem.category, // or keep String
                image = null,
            )
        )
        closeAdd()
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
}