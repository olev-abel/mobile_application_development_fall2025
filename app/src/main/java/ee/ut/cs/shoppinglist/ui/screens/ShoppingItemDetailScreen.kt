package ee.ut.cs.shoppinglist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.ui.components.itemdetails.ItemDetailsExternalSearchButton
import ee.ut.cs.shoppinglist.ui.components.itemdetails.ItemDetailsFacts
import ee.ut.cs.shoppinglist.ui.components.itemdetails.ItemDetailsHeaderImage
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.CONTENT_VISIBILITY_DELAY_MILLIS
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.FACTS_ELEVATION_ANIMATION_DURATION_MILLIS
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.FACTS_ELEVATION_HIDDEN
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.FACTS_ELEVATION_VISIBLE
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.FACTS_FADE_IN_DELAY_MILLIS
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.FACTS_FADE_IN_DURATION_MILLIS
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.FACTS_FADE_IN_OFFSET_DIVISOR
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.HEADER_IMAGE_FADE_IN_DURATION_MILLIS
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.HEADER_IMAGE_FADE_IN_OFFSET_DIVISOR
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.PADDING_MEDIUM
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.SEARCH_BUTTON_FADE_IN_ANIMATION_DURATION_MILLIS
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.SEARCH_BUTTON_FADE_IN_DELAY_DURATION_MILLIS
import ee.ut.cs.shoppinglist.ui.screens.DetailScreenAnimationAndSizeConst.SEARCH_BUTTON_FADE_IN_OFFSET_DIVISOR
import ee.ut.cs.shoppinglist.ui.viewmodels.detail.ItemDetailsViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen( viewModel: ItemDetailsViewModel) {

    val HEIGHT_MEDIUM = 16.dp
    val HEIGHT_LARGE = 32.dp


    // control visibility and staggered entrance
    val contentVisibleState = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(CONTENT_VISIBILITY_DELAY_MILLIS)
        contentVisibleState.value = true
    }
    val visible = contentVisibleState.value


    val factsElevation by animateDpAsState(
        targetValue = if (visible) FACTS_ELEVATION_VISIBLE else FACTS_ELEVATION_HIDDEN,
        animationSpec = tween(durationMillis = FACTS_ELEVATION_ANIMATION_DURATION_MILLIS)
    )

    val pageBackground = Color(0xFFF2F2F6) // light grey


    val item = viewModel.shoppingItem.collectAsState().value ?: return
    Scaffold(
         containerColor = pageBackground,
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
            // Header image: fade + slide in
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(HEADER_IMAGE_FADE_IN_DURATION_MILLIS)) +
                        slideInVertically(initialOffsetY = { it / HEADER_IMAGE_FADE_IN_OFFSET_DIVISOR }, animationSpec = tween(HEADER_IMAGE_FADE_IN_DURATION_MILLIS))
            ){
                ItemDetailsHeaderImage(item)
            }
            Spacer(Modifier.height(HEIGHT_MEDIUM))
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(FACTS_FADE_IN_DURATION_MILLIS, delayMillis = FACTS_FADE_IN_DELAY_MILLIS)) +
                        slideInVertically(initialOffsetY = { it / FACTS_FADE_IN_OFFSET_DIVISOR }, animationSpec = tween(FACTS_FADE_IN_DURATION_MILLIS, delayMillis = FACTS_FADE_IN_DELAY_MILLIS))
            ){
                Surface(
                    color = Color.White, // make the surface white
                    tonalElevation = factsElevation,
                    shape = RoundedCornerShape(PADDING_MEDIUM), // rounded corners
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PADDING_MEDIUM)
                        .animateContentSize()
                ){
                    ItemDetailsFacts(item)
                }

            }





            Spacer(Modifier.height(HEIGHT_MEDIUM))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec =tween(SEARCH_BUTTON_FADE_IN_ANIMATION_DURATION_MILLIS, delayMillis = SEARCH_BUTTON_FADE_IN_DELAY_DURATION_MILLIS)) +
                        slideInVertically(initialOffsetY = { it / SEARCH_BUTTON_FADE_IN_OFFSET_DIVISOR }, animationSpec = tween(SEARCH_BUTTON_FADE_IN_ANIMATION_DURATION_MILLIS, delayMillis = SEARCH_BUTTON_FADE_IN_DELAY_DURATION_MILLIS))
            ){
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ItemDetailsExternalSearchButton(item)
                }
            }
            Spacer(Modifier.height(HEIGHT_LARGE))
        }
    }
}

private object DetailScreenAnimationAndSizeConst {

    const val CONTENT_VISIBILITY_DELAY_MILLIS = 120L
    const val FACTS_ELEVATION_ANIMATION_DURATION_MILLIS = 300
    const val SEARCH_BUTTON_FADE_IN_ANIMATION_DURATION_MILLIS = 3000
    const val SEARCH_BUTTON_FADE_IN_DELAY_DURATION_MILLIS = 140

    const val HEADER_IMAGE_FADE_IN_DURATION_MILLIS = 3600
    const val HEADER_IMAGE_FADE_IN_OFFSET_DIVISOR = 6

    const val SEARCH_BUTTON_FADE_IN_OFFSET_DIVISOR = 4
    const val FACTS_FADE_IN_DURATION_MILLIS = 3200
    const val FACTS_FADE_IN_DELAY_MILLIS = 80
    const val FACTS_FADE_IN_OFFSET_DIVISOR = 5
    val FACTS_ELEVATION_VISIBLE = 6.dp
    val FACTS_ELEVATION_HIDDEN = 0.dp

    val PADDING_MEDIUM = 12.dp
    val HEIGHT_MEDIUM = 16.dp
    val HEIGHT_LARGE = 32.dp
}