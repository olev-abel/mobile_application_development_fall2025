package ee.ut.cs.shoppinglist.ui.viewmodels.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.common.isValidUrl
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository.ShoppingItemDetailsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ItemDetailsViewModel(
    private val navCoordinator: NavCoordinator,
    private val itemId: String,
    private val repository: ShoppingItemDetailsRepository,
) : ViewModel() {

    val shoppingItem = repository.getById(itemId)
        .map { item ->
            val validImage = item?.image?.takeIf { isValidUrl(it) }
            item?.copy(image = validImage)
        }
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = null)

    fun onBack() {
        navCoordinator.back()
    }
}