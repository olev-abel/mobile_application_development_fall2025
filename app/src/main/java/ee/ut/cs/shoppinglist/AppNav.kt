package ee.ut.cs.shoppinglist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ee.ut.cs.shoppinglist.ui.screens.DetailScreen
import ee.ut.cs.shoppinglist.ui.screens.ShoppingListScreen
import ee.ut.cs.shoppinglist.ui.viewmodels.NavEvent
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel


@Composable
fun AppNav(viewModel: ShoppingListViewModel) {
    val navController = rememberNavController()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // Single collector for channel events
    LaunchedEffect(navController) {
        viewModel.navEvents
            .flowWithLifecycle(lifecycle) // or repeatOnLifecycle(STARTED)
            .collect { event ->
                when (event) {
                    is NavEvent.ToItemDetail ->
                        navController.navigate(Screen.ItemDetailScreen.passId(event.id))
                    NavEvent.Back ->
                        navController.popBackStack()
                }
            }
    }

    NavHost(navController, startDestination = Screen.ListScreen.route) {
        composable(Screen.ListScreen.route) {
            ShoppingListScreen(viewModel = viewModel) // no nav lambdas
        }
        composable(
            route = Screen.ItemDetailScreen.route,
            arguments = listOf(navArgument(ITEM_DETAIL_SCREEN_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(ITEM_DETAIL_SCREEN_ID)!!
            DetailScreen(id = id, viewModel = viewModel) // no onBack; VM handles it
        }
    }
}
