package ee.ut.cs.shoppinglist

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ee.ut.cs.shoppinglist.ui.screens.DetailScreen
import ee.ut.cs.shoppinglist.ui.screens.ShoppingListScreen
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel


@Composable
fun AppNav(viewModel: ShoppingListViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.ListScreen.route) {
        composable(Screen.ListScreen.route) {
            ShoppingListScreen(
                viewModel = viewModel,
                onOpenDetail = { id -> navController.navigate(Screen.ItemDetailScreen.passId(id)) }
            )
        }
        composable(
            route = Screen.ItemDetailScreen.route,
            arguments = listOf(navArgument(ITEM_DETAIL_SCREEN_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(ITEM_DETAIL_SCREEN_ID)!!
            DetailScreen(id = id, onBack = { navController.popBackStack() }, viewModel = viewModel)
        }
    }
}
