package ee.ut.cs.shoppinglist.data

import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.Screen

class TestNavCoordinator : NavCoordinator() {
    var lastRoute: String? = null
        private set
    var navigateCount: Int = 0
        private set

    override fun toDetailScreen(id: String) {
        super.toDetailScreen(id)
        lastRoute = Screen.ItemDetailScreen.passId(id)
        navigateCount++
    }
    override fun toListScreen() {
        super.toListScreen()
        lastRoute = Screen.ListScreen.route
        navigateCount++
    }
    override fun back() {
        super.back()
        lastRoute = "Back"
        navigateCount++
    }
    override fun logout() {
        super.logout()
        lastRoute = "Logout"
        navigateCount++
    }
}