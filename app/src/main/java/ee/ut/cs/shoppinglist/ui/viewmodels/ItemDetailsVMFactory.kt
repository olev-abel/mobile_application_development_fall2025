package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem


class ItemDetailsVmFactory(
    private val navCoordinator: NavCoordinator,
    private val item: ShoppingItem
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemDetailsViewModel(
            navCoordinator = navCoordinator,
            item = item
        ) as T
    }


}