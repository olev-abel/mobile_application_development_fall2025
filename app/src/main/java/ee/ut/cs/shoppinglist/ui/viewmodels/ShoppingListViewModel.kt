package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.lifecycle.ViewModel
import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShoppingListViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items = _items.asStateFlow()

    init {
        _items.value = listOf(
            ShoppingItem(name = "Milk", quantity = 1, category = ShoppingCategory.DAIRY),
            ShoppingItem(name = "Apples", quantity = 6, category = ShoppingCategory.FRUITS)
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