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

class ShoppingListViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private companion object { const val KEY_VIEW = "view_mode" }
    var viewMode by mutableStateOf(savedStateHandle[KEY_VIEW] ?: ViewMode.All)
        private set

    fun toggleViewMode(mode: ViewMode) {
        viewMode = mode
        savedStateHandle[KEY_VIEW] = mode
    }
    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items = _items.asStateFlow()

    init {
        _items.value = listOf(
            ShoppingItem(name = R.plurals.milks, quantity = 1, category = ShoppingCategory.DAIRY, image = R.drawable.milk),
            ShoppingItem(name = R.plurals.apples, quantity = 6, category = ShoppingCategory.FRUITS, image = R.drawable.apple)
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
}