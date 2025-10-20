package ee.ut.cs.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.ui.components.itemdetails.ItemDetailsExternalSearchButton
import ee.ut.cs.shoppinglist.ui.components.itemdetails.ItemDetailsFacts
import ee.ut.cs.shoppinglist.ui.components.itemdetails.ItemDetailsHeaderImage
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.ItemDetailsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: String, viewModel: ItemDetailsViewModel) {

    val item = viewModel.shoppingItem.collectAsState().value ?: return
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ItemDetailsHeaderImage(item)
            Spacer(Modifier.height(16.dp))

            ItemDetailsFacts(item)

            Spacer(Modifier.height(16.dp))

            ItemDetailsExternalSearchButton(item)

            Spacer(Modifier.height(32.dp))
        }
    }
}
