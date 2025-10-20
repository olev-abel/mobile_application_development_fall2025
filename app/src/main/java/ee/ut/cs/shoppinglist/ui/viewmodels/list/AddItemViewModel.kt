package ee.ut.cs.shoppinglist.ui.viewmodels.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class NewItemUi(
    val name: String = "",
    val quantity: String = "",
    val category: ShoppingCategory = ShoppingCategory.MISC
)

class AddItemViewModel : ViewModel() {

    var showAddDialog by mutableStateOf(false)

    private val _newItem = MutableStateFlow(NewItemUi())
    val newItem = _newItem.asStateFlow()

    fun updateName(newName: String) {
        _newItem.update {
            it.copy(name = newName)
        }
    }

    fun updateQuantity(newQuantity: String) {
        _newItem.update {
            it.copy(quantity = newQuantity)
        }
    }
    fun updateCategory(newCategory: ShoppingCategory) {
        _newItem.update {
            it.copy(category = newCategory)
        }
    }
    fun openAdd() {
        showAddDialog = true
    }

    fun closeAdd() {
        showAddDialog = false
    }
}