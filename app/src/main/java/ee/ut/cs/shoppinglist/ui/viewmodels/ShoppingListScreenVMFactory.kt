package ee.ut.cs.shoppinglist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import ee.ut.cs.shoppinglist.NavCoordinator

class ShoppingListScreenVMFactory(
    private val navCoordinator: NavCoordinator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val handle = extras.createSavedStateHandle()
        return ShoppingListViewModel(savedStateHandle = handle, navCoordinator) as T
    }
}