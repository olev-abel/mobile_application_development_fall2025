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
    NavHost(navController, startDestination = "list") {
        composable("list") {
            ShoppingListScreen(
                viewModel = viewModel,
                onOpenDetail = { id -> navController.navigate("detail/$id") }
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!
            DetailScreen(id = id, onBack = { navController.popBackStack() }, viewModel = viewModel)
        }
    }
}
