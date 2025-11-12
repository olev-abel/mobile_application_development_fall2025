package ee.ut.cs.shoppinglist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ee.ut.cs.shoppinglist.common.ResourceProvider
import ee.ut.cs.shoppinglist.domain.authentication.AuthenticationRepository
import ee.ut.cs.shoppinglist.ui.screens.DetailScreen
import ee.ut.cs.shoppinglist.ui.screens.LoginScreen
import ee.ut.cs.shoppinglist.ui.screens.ShoppingListScreen
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.ItemDetailsVmFactory
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository.ShoppingItemDetailsRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.list.ShoppingListScreenVMFactory
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ViewModeRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.login.LoginVMFactory


@Composable
fun AppNav(
    navCoordinator: NavCoordinator,
    shoppingListRepository: ShoppingListRepository,
    shoppingItemDetailsRepository: ShoppingItemDetailsRepository,
    viewModeRepository: ViewModeRepository,
    authenticationRepository: AuthenticationRepository,
    resourceProvider: ResourceProvider
) {

    val navController = rememberNavController()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val startDestination = authenticationRepository.isUserLoggedIn()
        .let { if (it) Screen.ListScreen.route else Screen.LoginScreen.route }
    LaunchedEffect(navController) {
        navCoordinator.events
            .flowWithLifecycle(lifecycle) // or repeatOnLifecycle(STARTED)
            .collect { event ->
                when (event) {
                    is NavEvent.ToDetailScreen ->
                        navController.navigate(Screen.ItemDetailScreen.passId(event.id))

                    NavEvent.Back ->
                        navController.popBackStack()

                    NavEvent.ToListScreen -> navController.navigate(Screen.ListScreen.route)
                    NavEvent.Logout -> navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
    }
    NavHost(navController, startDestination = startDestination) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                viewModel = viewModel(
                    factory = LoginVMFactory(
                        navCoordinator,
                        authenticationRepository,
                        resourceProvider
                    )
                ),
            )
        }
        composable(Screen.ListScreen.route) {
            ShoppingListScreen(
                viewModel = viewModel(
                    factory = ShoppingListScreenVMFactory(
                        navCoordinator,
                        shoppingListRepository,
                        viewModeRepository,
                        authenticationRepository,
                        resourceProvider
                    )
                ),
            )
        }
        composable(
            route = Screen.ItemDetailScreen.route,
            arguments = listOf(navArgument(ITEM_DETAIL_SCREEN_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(ITEM_DETAIL_SCREEN_ID)!!
            DetailScreen(
                viewModel = viewModel(
                    factory = ItemDetailsVmFactory(
                        navCoordinator,
                        itemId = id,
                        shoppingItemDetailsRepository
                    )
                )
            )
        }
    }
}
