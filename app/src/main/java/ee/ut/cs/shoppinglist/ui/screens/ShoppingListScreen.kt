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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.ui.components.shoppingitemlist.AddItemDialog
import ee.ut.cs.shoppinglist.ui.components.shoppingitemlist.ShoppingListRow
import ee.ut.cs.shoppinglist.ui.viewmodels.AddItemViewModel
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel


@Composable
fun SearchBar(vm: ShoppingListViewModel) {
    val searchQuery = vm.query.collectAsState().value
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { vm.updateSearchQuery(it) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        placeholder = { Text("Search items") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        singleLine = true
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel
) {

    val viewMode by viewModel.viewMode.collectAsState()
    val addVm: AddItemViewModel = viewModel(key = "AddItemVM")
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            addVm.openAdd()
        }) { Icon(Icons.Default.Add, stringResource(R.string.btn_add)) }
    }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            SearchBar(viewModel)
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

            when (viewMode) {
                ViewMode.All -> AllItemsList(viewModel)
                ViewMode.ByCategory -> CategoryList(viewModel)
            }
            AddItemDialog(addVm, {
                viewModel.saveNewItem(addVm.newItem.value)
            })
        }
    }

}

enum class ViewMode { All, ByCategory }


@Composable
fun AllItemsList(viewModel: ShoppingListViewModel) {
    val filteredItems by viewModel.filteredItems.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredItems, key = { it.id }) { item ->
            ShoppingListRow(
                item = item,
                onCheckChanged = { viewModel.toggleBought(item) },
                onRemove = { viewModel.removeItem(item) },
                onClick = {item -> viewModel.openItemDetail(item.id)})
        }
    }
}

@Composable
fun CategoryList(viewModel: ShoppingListViewModel) {
    val filteredItems by viewModel.filteredItems.collectAsState()
    val grouped = filteredItems.groupBy { it.category }

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
                    onRemove = { viewModel.removeItem(item) },
                    onClick = {item -> viewModel.openItemDetail(item.id)})
            }
        }
    }
}

