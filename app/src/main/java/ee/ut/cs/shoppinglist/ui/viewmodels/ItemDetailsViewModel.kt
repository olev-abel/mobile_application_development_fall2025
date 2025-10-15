package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.lifecycle.ViewModel
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow

class ItemDetailsViewModel(
    private val navCoordinator: NavCoordinator,
    item: ShoppingItem
) : ViewModel() {

    val shoppingItem = MutableStateFlow(item)

    fun onBack() {
        navCoordinator.back()
    }
}