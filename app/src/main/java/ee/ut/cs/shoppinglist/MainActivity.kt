package ee.ut.cs.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.shoppinglist.ui.screens.ShoppingListScreen
import ee.ut.cs.shoppinglist.ui.theme.ShoppingListTheme
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: ShoppingListViewModel = viewModel()
            ShoppingListTheme {
                ShoppingListScreen(vm)
            }
        }
    }
}

