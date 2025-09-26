package ee.ut.cs.shoppinglist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem


@Composable
fun QuantityPill(qty: Int, modifier: Modifier = Modifier) {
    val pillRadius = dimensionResource(R.dimen.pill_corner_radius)
    val pillPadH = dimensionResource(R.dimen.pill_pad_h)
    val pillPadV = dimensionResource(R.dimen.pill_pad_v)

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(pillRadius)
            )
            .padding(horizontal = pillPadH, vertical = pillPadV)
    ) {
        Text(
            text = "x$qty",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ShoppingListRow(
    item: ShoppingItem,
    modifier: Modifier = Modifier,
    onCheckChanged: (Boolean) -> Unit,
    onRemove: (ShoppingItem) -> Unit
) {
    val rowPadding = dimensionResource(R.dimen.row_padding)
    val avatarSize = dimensionResource(R.dimen.avatar_size)
    val gapXs = dimensionResource(R.dimen.spacing_xs)
    val gapSm = dimensionResource(R.dimen.spacing_sm)
    val gapMd = dimensionResource(R.dimen.spacing_md)

    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) onRemove(item)
            it != SwipeToDismissBoxValue.StartToEnd
        }
    )
    SwipeToDismissBox(
        state = swipeToDismissState,
        modifier = Modifier.fillMaxSize(),
        backgroundContent = {
            when (swipeToDismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {}
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove item",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }
                SwipeToDismissBoxValue.Settled -> {}
            }
        }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(rowPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.image != null) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null
                    )
                }
            }

            Spacer(Modifier.width(gapSm))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        style = if (item.isBought) {
                            MaterialTheme.typography.titleMedium.copy(
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )
                        } else {
                            MaterialTheme.typography.titleMedium
                        }
                    )
                    Spacer(Modifier.width(gapSm))
                    QuantityPill(qty = item.quantity)
                }

                Spacer(Modifier.height(gapXs))

                Text(
                    text = item.category.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.width(gapMd))

            Checkbox(
                checked = item.isBought,
                onCheckedChange = onCheckChanged
            )
        }
    }

}