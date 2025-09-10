package ee.ut.cs.shoppinglist.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem

@Composable
fun ShoppingListRow(
    item: ShoppingItem,
    onToggleBought: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = item.isBought, onCheckedChange = { onToggleBought() })
        Spacer(Modifier.width(8.dp))
        Text (item.name)
        Spacer (Modifier.width(8.dp))
        Text ("Qty: ${item.quantity}")
    }
}