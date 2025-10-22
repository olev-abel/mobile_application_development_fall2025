package ee.ut.cs.shoppinglist.ui.viewmodels.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ViewModeRepository

class ShoppingListScreenVMFactory(
    private val navCoordinator: NavCoordinator,
    private val repository: ShoppingListRepository,
    private val viewModeRepository: ViewModeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val handle = extras.createSavedStateHandle()
        return ShoppingListViewModel(
            savedStateHandle = handle,
            navCoordinator = navCoordinator,
            listRepository = repository,
            viewModeRepository = viewModeRepository
        ) as T
    }
}