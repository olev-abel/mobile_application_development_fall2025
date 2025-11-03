package ee.ut.cs.shoppinglist.ui.components.itemdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.components.shoppingitemlist.QuantityPill


@Composable
fun ItemDetailsFacts(item: ShoppingItem) {
    val PADDING_MEDIUM = 16.dp
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = PADDING_MEDIUM),
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
}

