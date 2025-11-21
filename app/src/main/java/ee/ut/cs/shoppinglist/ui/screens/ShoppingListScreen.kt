package ee.ut.cs.shoppinglist.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.ui.components.shoppingitemlist.AddItemDialog
import ee.ut.cs.shoppinglist.ui.components.shoppingitemlist.ShoppingListRow
import ee.ut.cs.shoppinglist.ui.viewmodels.list.AddItemViewModel
import ee.ut.cs.shoppinglist.ui.viewmodels.list.ShoppingListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private object Dimensions {
    val PADDING_LARGE = 16.dp
    val PADDING_MEDIUM = 8.dp
}

@Composable
fun SearchBar(vm: ShoppingListViewModel) {
    OutlinedTextField(
        value = vm.query.collectAsState().value,
        onValueChange = { vm.updateSearchQuery(it) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        placeholder = { Text("Search items") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimensions.PADDING_LARGE, vertical = Dimensions.PADDING_MEDIUM),
        singleLine = true
    )
}

@Composable
fun AnimatedFab(onClick: () -> Unit, icon: @Composable () -> Unit) {
    val PRESSED_SCALE = 0.92f
    val DEFAULT_SCALE = 1f
    val PRESSED_ANIMATION_DURATION = 120L
    var pressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(targetValue = if (pressed) PRESSED_SCALE else DEFAULT_SCALE, animationSpec = spring())

    FloatingActionButton(
        onClick = {
            scope.launch {
                pressed = true
                delay(PRESSED_ANIMATION_DURATION)
                onClick()
                pressed = false
            }
        },
        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        icon()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel
) {

    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ShoppingListViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    val viewMode by viewModel.viewMode.collectAsState()
    val addVm: AddItemViewModel = viewModel(key = "AddItemVM")
    Scaffold(floatingActionButton = {
        AnimatedFab(onClick = { addVm.openAdd()} ) {
            Icon(Icons.Default.Add, stringResource(R.string.btn_add))
        }
    }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Row {
                Spacer(Modifier.weight(1f))
                IconButton(modifier = Modifier.size(80.dp), onClick = {
                    viewModel.logout()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout, contentDescription = null,
                        modifier = Modifier
                            .padding(
                                top = Dimensions.PADDING_LARGE,
                                bottom = Dimensions.PADDING_LARGE
                            )
                            .fillMaxWidth()
                            .then(Modifier),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

            }
            SearchBar(viewModel)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.PADDING_MEDIUM),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.PADDING_MEDIUM)
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
                onClick = {item ->viewModel.openDetailScreen(item.id)})
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
                        .padding(
                            horizontal = Dimensions.PADDING_LARGE,
                            vertical = Dimensions.PADDING_MEDIUM
                        )
                )
            }
            items(itemsInCategory, key = { it.id }) { item ->
                ShoppingListRow(
                    item = item,
                    onCheckChanged = { viewModel.toggleBought(item) },
                    onRemove = { viewModel.removeItem(item) },
                    onClick = { item -> viewModel.openDetailScreen(item.id) })
            }
        }
    }
}
