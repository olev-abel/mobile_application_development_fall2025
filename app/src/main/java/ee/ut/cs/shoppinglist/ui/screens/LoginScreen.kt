package ee.ut.cs.shoppinglist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.ENTRANCE_ANIMATION_DELAY_MS
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.FORM_FADE_IN_ANIMATION_DURATION_MS
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.FORM_FADE_IN_OFFSET_DIVIDER
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_ENTRANCE_ANIMATION_DURATION_1
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_ENTRANCE_ANIMATION_DURATION_2
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_ENTRANCE_ANIMATION_DURATION_3
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_ENTRANCE_ANIMATION_DURATION_4
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_1
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_2
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_3
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.INITIAL_FORM_OFFSET
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.LOADING_INDICATOR_SIZE
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.LOADING_INDICATOR_STROKE_WIDTH
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.LOGO_FADE_IN_ANIMATION_DURATION_MS
import ee.ut.cs.shoppinglist.ui.screens.ScreenAnimationAndSizeConst.LOGO_FADE_IN_OFFSET_DIVIDER
import ee.ut.cs.shoppinglist.ui.viewmodels.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val PADDING_SMALL = 12.dp
    val PADDING_NORMAL = 16.dp

    // entrance visibility
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(ENTRANCE_ANIMATION_DELAY_MS) // small stagger
        contentVisible = true
    }
    // SIMPLE LOGO

    val logoScale by animateFloatAsState(
        targetValue = if (contentVisible) ScreenAnimationAndSizeConst.LOGO_SCALE_DEFAULT else ScreenAnimationAndSizeConst.LOGO_SCALE_INITIAL,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )

    ) {

    }

    val density = LocalDensity.current
    val formOffset = remember { Animatable(INITIAL_FORM_OFFSET) }




    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginViewModel.UiEvent.ShowError -> {
                    loading.value = false
                    launch {
                        val px = with(density) { PADDING_SMALL.toPx() }
                        formOffset.animateTo(px, tween(INITIAL_ENTRANCE_ANIMATION_DURATION_1))
                        //1 time left
                        formOffset.animateTo(
                            -px * INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_1,
                            tween(INITIAL_ENTRANCE_ANIMATION_DURATION_1)
                        )
                        // 2right
                        formOffset.animateTo(
                            px * INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_2,
                            tween(INITIAL_ENTRANCE_ANIMATION_DURATION_2)
                        )
                        // 3left
                        formOffset.animateTo(
                            -px * INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_3,
                            tween(INITIAL_ENTRANCE_ANIMATION_DURATION_3)
                        )
                        formOffset.animateTo(
                            INITIAL_FORM_OFFSET,
                            tween(INITIAL_ENTRANCE_ANIMATION_DURATION_4)
                        )
                    }
                    snackbarHostState.showSnackbar(event.message)
                }

                is LoginViewModel.UiEvent.Loading -> {
                    loading.value = true
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = { TopAppBar(title = { Text("Login") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(PADDING_NORMAL),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo with entrance scale + fade/slide
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(LOGO_FADE_IN_ANIMATION_DURATION_MS)) + slideInVertically(
                    initialOffsetY = { it / LOGO_FADE_IN_OFFSET_DIVIDER },
                    animationSpec = tween(LOGO_FADE_IN_ANIMATION_DURATION_MS)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "App logo",
                    modifier = Modifier
                        .size(96.dp)
                        .scale(logoScale)
                        .padding(bottom = PADDING_SMALL)
                )
            }

            // Form block: slides/fades in and translates when shaken
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(FORM_FADE_IN_ANIMATION_DURATION_MS)) + slideInVertically(
                    initialOffsetY = { it / FORM_FADE_IN_OFFSET_DIVIDER },
                    animationSpec = tween(FORM_FADE_IN_ANIMATION_DURATION_MS)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { translationX = formOffset.value }

                ) {
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text(stringResource(R.string.email_field_hint)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text(stringResource(R.string.password_field_hint)) },
                        singleLine = true,
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(
                                    imageVector = if (passwordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible.value) stringResource(R.string.hide_password_description) else stringResource(
                                        R.string.show_password_description
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = PADDING_SMALL)
                    )

                    Button(
                        onClick = {
                            if (!loading.value) {
                                // trigger press animation and login
                                viewModel.onLoginClicked(email.value, password.value)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = PADDING_NORMAL),
                        contentPadding = PaddingValues(vertical = PADDING_SMALL)
                    ) {

                        // Crossfade between label and progress indicator
                        Crossfade(targetState = loading.value) { isLoading ->
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(LOADING_INDICATOR_SIZE),
                                    strokeWidth = LOADING_INDICATOR_STROKE_WIDTH,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(stringResource(R.string.btn_login))
                            }
                        }
                    }
                }
            }
        }
    }
}








private object ScreenAnimationAndSizeConst {

    val INITIAL_FORM_OFFSET = 0f

    val LOGO_SCALE_DEFAULT = 1f
    val LOGO_SCALE_INITIAL = 0.9f

    val PADDING_SMALL = 12.dp
    val PADDING_NORMAL = 16.dp
    val ENTRANCE_ANIMATION_DELAY_MS = 180L
    val INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_1 = 0.75f
    val INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_2 = 0.5f
    val INITIAL_ENTRANCE_ANIMATION_OFFSET_MULTIPLIER_3 = 0.3f

    val INITIAL_ENTRANCE_ANIMATION_DURATION_1 = 60
    val INITIAL_ENTRANCE_ANIMATION_DURATION_2 = 50
    val INITIAL_ENTRANCE_ANIMATION_DURATION_3 = 40
    val INITIAL_ENTRANCE_ANIMATION_DURATION_4 = 30

    val LOGO_FADE_IN_OFFSET_DIVIDER = 4

    val LOGO_FADE_IN_ANIMATION_DURATION_MS = 300
    val FORM_FADE_IN_ANIMATION_DURATION_MS = 320
    val FORM_FADE_IN_OFFSET_DIVIDER = 6

    val LOADING_INDICATOR_SIZE = 20.dp
    val LOADING_INDICATOR_STROKE_WIDTH = 2.dp
}
