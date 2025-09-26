package ee.ut.cs.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.components.AddItemDialog
import ee.ut.cs.shoppinglist.ui.components.ShoppingListRow
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.openAdd()
        }) { Icon(Icons.Default.Add, stringResource(R.string.btn_add)) }
    }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { viewModel.toggleViewMode(ViewMode.All) }
                ) { Text(stringResource(R.string.btn_category_all)) }
                Button(onClick = { viewModel.toggleViewMode(ViewMode.ByCategory) }
                ) { Text(stringResource(R.string.btn_category_by_category)) }
            }

            when (viewModel.viewMode) {
                ViewMode.All -> AllItemsList(viewModel)
                ViewMode.ByCategory -> CategoryList(viewModel)
            }
            AddItemDialog(viewModel)
        }
    }

}

enum class ViewMode { All, ByCategory }


@Composable
fun AllItemsList(viewModel: ShoppingListViewModel) {
    val items by viewModel.items.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { item ->
            ShoppingListRow(
                item = item,
                onCheckChanged = { viewModel.toggleBought(item) },
                onRemove = { viewModel.removeItem(item) })
        }
    }
}

@Composable
fun CategoryList(viewModel: ShoppingListViewModel) {
    val items by viewModel.items.collectAsState()
    val grouped = items.groupBy { it.category }

    LazyColumn(Modifier.fillMaxSize()) {
        grouped.forEach { (category, itemsInCategory) ->
            item {
                Text(
                    text = category.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(itemsInCategory, key = { it.id }) { item ->
                ShoppingListRow(
                    item = item,
                    onCheckChanged = { viewModel.toggleBought(item) },
                    onRemove = { viewModel.removeItem(item) })
            }
        }
    }
}

