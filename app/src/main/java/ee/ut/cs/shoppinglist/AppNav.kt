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
import ee.ut.cs.shoppinglist.ui.screens.DetailScreen
import ee.ut.cs.shoppinglist.ui.screens.ShoppingListScreen
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.ItemDetailsVmFactory
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.repository.ShoppingItemDetailsRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ShoppingListRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.list.ShoppingListScreenVMFactory
import ee.ut.cs.shoppinglist.ui.viewmodels.list.repository.ViewModeRepository


@Composable
fun AppNav(
    navCoordinator: NavCoordinator,
    shoppingListRepository: ShoppingListRepository,
    viewModeRepository: ViewModeRepository,
    shoppingItemDetailsRepository: ShoppingItemDetailsRepository
) {
    val navController = rememberNavController()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(navController) {
        navCoordinator.events
            .flowWithLifecycle(lifecycle) // or repeatOnLifecycle(STARTED)
            .collect { event ->
                when (event) {
                    is NavEvent.ToDetailScreen ->
                        navController.navigate(Screen.ItemDetailScreen.passId(event.id))

                    NavEvent.Back ->
                        navController.popBackStack()

                }
            }
    }
    NavHost(navController, startDestination = Screen.ListScreen.route) {
        composable(Screen.ListScreen.route) {
            ShoppingListScreen(
                viewModel = viewModel(
                    factory = ShoppingListScreenVMFactory(
                        navCoordinator = navCoordinator,
                        listRepository = shoppingListRepository,
                        viewModeRepository = viewModeRepository,
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
                id = id,
                viewModel = viewModel(
                    factory = ItemDetailsVmFactory(
                        navCoordinator = navCoordinator,
                        itemId = id, repository = shoppingItemDetailsRepository
                    )
                )
            )
        }
    }
}
