package ee.ut.cs.shoppinglist.ui.components.itemdetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem


@Composable
fun ItemDetailsExternalSearchButton(item: ShoppingItem) {
    val PADDING_LARGE = 16.dp
    val PADDING_MEDIUM = 8.dp
    val context = LocalContext.current
    Button(
        onClick = {
            val q = Uri.encode(item.name)
            val intent =
                Intent(Intent.ACTION_VIEW, (GOOGLE_SEARCH_BASE_URL + q).toUri())
            context.startActivity(intent)
        },
        modifier = Modifier
            .padding(horizontal = PADDING_LARGE)
            .fillMaxWidth()
    ) {
        Icon(
            Icons.Default.Search, contentDescription = null,

            )
        Spacer(Modifier.width(PADDING_MEDIUM))
        Text(stringResource(R.string.btn_search_on_google))
    }
}
private const val GOOGLE_SEARCH_BASE_URL = "https://www.google.com/search?q="