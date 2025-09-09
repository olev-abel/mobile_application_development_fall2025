package ee.ut.cs.shoppinglist.ui.viewmodel

import androidx.lifecycle.ViewModel
import ee.ut.cs.shoppinglist.domain.model.Category
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShoppingListViewModel: ViewModel() {

    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items = _items.asStateFlow()

    init {
        // Dummy data for now
        _items.value = listOf(
            ShoppingItem(name = "Milk", quantity = 1, category = Category.DAIRY),
            ShoppingItem(name = "Apples", quantity = 6, category = Category.FRUITS)
        )
    }

    fun addItem(item: ShoppingItem) {
        _items.value = _items.value + item
    }

    fun removeItem(item: ShoppingItem) {
        _items.value = _items.value - item
    }

    fun updateItem(updated: ShoppingItem) {
        _items.value = _items.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    fun toggleBought(item: ShoppingItem) {
        updateItem(item.copy(isBought = !item.isBought))
    }
}