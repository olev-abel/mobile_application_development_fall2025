package ee.ut.cs.shoppinglist.ui.components.shoppingitemlist

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.launch


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
    onRemove: (ShoppingItem) -> Unit,
    onClick: (ShoppingItem) -> Unit
) {

    val CHECKBOX_SELECTED_SCALE = 1.2f
    val CHECKBOX_DEFAULT_SCALE = 1f
    val checkboxScale by animateFloatAsState(
        targetValue = if (item.isBought) CHECKBOX_SELECTED_SCALE else CHECKBOX_DEFAULT_SCALE,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val TEXT_ALPHA_BOUGHT = 0.5f
    val TEXT_ALPHA_DEFAULT = 1f
    val TEXT_ALPHA_ANIMATION_DURATION = 300
    val textAlpha by animateFloatAsState(
        targetValue = if (item.isBought) TEXT_ALPHA_BOUGHT else TEXT_ALPHA_DEFAULT,
        animationSpec = tween(TEXT_ALPHA_ANIMATION_DURATION)
    )


    val rowPadding = dimensionResource(R.dimen.row_padding)
    val gapXs = dimensionResource(R.dimen.spacing_xs)
    val gapSm = dimensionResource(R.dimen.spacing_sm)
    val gapMd = dimensionResource(R.dimen.spacing_md)

    val swipeToDismissState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()

    val ICON_SCALE_ENLARGED = 1.2f
    val ICON_SCALE_DEFAULT = 1f
    val SWIPE_PROGRESS_THRESHOLD = 0.15f

    var iconTarget by remember { mutableStateOf(ICON_SCALE_DEFAULT) }
    val iconScale by animateFloatAsState(
        targetValue = iconTarget,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    LaunchedEffect(swipeToDismissState) {
        snapshotFlow { swipeToDismissState.progress to swipeToDismissState.dismissDirection }
            .collect { (progress, dir) ->
                iconTarget = if (dir == SwipeToDismissBoxValue.EndToStart && progress > SWIPE_PROGRESS_THRESHOLD) ICON_SCALE_ENLARGED else ICON_SCALE_DEFAULT
            }
    }



    SwipeToDismissBox(
        state = swipeToDismissState,
        modifier = Modifier.fillMaxSize(),
        backgroundContent = {
            if (swipeToDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                        .padding(rowPadding),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cont_dsc_remove_item),
                        modifier = Modifier
                            .size(28.dp)
                            .graphicsLayer { scaleX = iconScale; scaleY = iconScale },
                        tint = Color.White
                    )
                }
            }
        },
        onDismiss = { direction ->
            when (direction) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(item)
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    scope.launch {
                        swipeToDismissState.reset()
                    }
                }

                SwipeToDismissBoxValue.Settled -> {}
            }
        }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(rowPadding)
                .clickable { onClick(item) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShoppingItemAvatar(item)

            Spacer(Modifier.width(gapSm))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        modifier = Modifier.graphicsLayer(alpha = textAlpha),
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
                onCheckedChange = onCheckChanged,
                modifier = Modifier.scale(checkboxScale)
            )
        }
    }

}

@Composable
fun ShoppingItemAvatar(
    item: ShoppingItem,
    size: Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val loadFailed = remember { mutableStateOf(false) }

    val hasHttpUrl = item.image?.let {
        runCatching { it.toUri().scheme?.startsWith("http") == true }.getOrDefault(false)
    } ?: false

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (hasHttpUrl && !loadFailed.value) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.image)
                    .crossfade(true)
                    .build(),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
                onError = {
                    Log.e("ShoppingItemAvatar", "Image load failed for url: ${it}")
                    loadFailed.value = true },
                onSuccess = { loadFailed.value = false },
                loading =  {
                    CircularProgressIndicator(modifier = Modifier.requiredSize(40.dp))
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null
                )
            }
        }
    }
}