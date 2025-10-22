package ee.ut.cs.shoppinglist.ui.viewmodels.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository.ShoppingItemDetailsRepository


class ItemDetailsVmFactory(
    private val navCoordinator: NavCoordinator,
    private val itemId: String,
    private val repository: ShoppingItemDetailsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemDetailsViewModel(
            navCoordinator = navCoordinator,
            itemId = itemId,
            repository = repository
        ) as T
    }


}