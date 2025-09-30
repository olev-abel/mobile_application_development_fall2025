package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory


data class NewItemUi(
    val name: String = "This is item",
    val quantity: String = "1",
    val category: ShoppingCategory = ShoppingCategory.MISC
)

class AddItemViewModel : ViewModel() {

    var showAddDialog by mutableStateOf(false)
    var newItem by mutableStateOf(NewItemUi())
    var categories by mutableStateOf(ShoppingCategory.entries)

    fun openAdd() {
        showAddDialog = true
    }

    fun closeAdd() {
        showAddDialog = false
    }
}