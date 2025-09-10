package ee.ut.cs.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.components.ShoppingListRow
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    val items by viewModel.items.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Shopping List") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addItem(
                    ShoppingItem(
                        name = "Bananas",
                        quantity = 3,
                        category = ShoppingCategory.FRUITS
                    )
                )
            }) { Icon(Icons.Default.Add, "Add") }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Text("No items yet.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items) { item ->
                    ShoppingListRow(
                        item = item,
                        onToggleBought = { viewModel.toggleBought(item) })
                }
            }
        }
    }
}