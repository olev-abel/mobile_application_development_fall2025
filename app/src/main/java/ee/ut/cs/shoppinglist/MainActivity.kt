package ee.ut.cs.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ee.ut.cs.shoppinglist.ui.theme.ShoppingListTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = (application as ShoppingListApplication)
        setContent {


            ShoppingListTheme {
                AppNav(
                    navCoordinator = app.navCoordinator,
                    shoppingListRepository = app.shoppingListRepository,
                    viewModeRepository = app.viewModeRepository,
                    shoppingItemDetailsRepository = app.shoppingItemDetailsRepository
                )

            }
        }
    }
}

