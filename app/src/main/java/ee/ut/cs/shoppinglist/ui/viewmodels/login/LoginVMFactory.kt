package ee.ut.cs.shoppinglist.ui.viewmodels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.common.ResourceProvider
import ee.ut.cs.shoppinglist.domain.authentication.AuthenticationRepository

class LoginVMFactory(
    private val navCoordinator: NavCoordinator,
    private val authenticationRepository: AuthenticationRepository,
    private val resourceProvider: ResourceProvider
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

        return LoginViewModel(
            navCoordinator = navCoordinator,
            authenticationRepository = authenticationRepository,
            resources = resourceProvider
        ) as T
    }
}