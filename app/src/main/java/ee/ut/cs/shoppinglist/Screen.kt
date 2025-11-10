package ee.ut.cs.shoppinglist

const val ITEM_DETAIL_SCREEN_ID = "id";

sealed class Screen(val route: String) {

    object LoginScreen : Screen(route = "login_screen")
    object ListScreen : Screen(route = "list_screen")
    object ItemDetailScreen : Screen(route = "item_detail_screen/{$ITEM_DETAIL_SCREEN_ID}") {
        fun passId(id: String): String {
            return "item_detail_screen/$id"
        }
    }
}
