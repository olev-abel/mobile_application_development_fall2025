package ee.ut.cs.shoppinglist.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ee.ut.cs.shoppinglist.ui.components.QuantityPill
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: String, onBack: () -> Unit, viewModel: ShoppingListViewModel) {
    val item = viewModel.itemById(id)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            // Header image
            Box(Modifier.fillMaxWidth()) {
                val headerHeight = 180.dp
                if (item.image != null) {
                    Image(
                        painter = painterResource(item.image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerHeight),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(headerHeight)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Facts / status
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(item.name, style = MaterialTheme.typography.titleLarge)
                    Text(
                        item.category.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                QuantityPill(qty = item.quantity)
            }

            Spacer(Modifier.height(16.dp))

            // External info (open browser)
            val context = LocalContext.current
            Button(
                onClick = {
                    val q = Uri.encode(item.name)
                    val intent =
                        Intent(Intent.ACTION_VIEW, "https://www.google.com/search?q=$q".toUri())
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Search, contentDescription = null,

                    )
                Spacer(Modifier.width(8.dp))
                Text("Search on Google")
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
