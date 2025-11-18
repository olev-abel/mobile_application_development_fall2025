package ee.ut.cs.shoppinglist.ui.screens

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.common.TestResourceProvider
import ee.ut.cs.shoppinglist.common.TestTags
import ee.ut.cs.shoppinglist.data.TestAuthenticationRepository
import ee.ut.cs.shoppinglist.ui.viewmodels.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    // Test double for the ViewModel. The project provides a test-only open LoginViewModel()
    // in the androidTest source set (no-arg). Extend that instead of trying to call the
    // production constructor with test-only dependencies.
    private class TestLoginViewModel(shouldLoginSucceed: Boolean, shouldUserBeLoggedIn: Boolean) :
        LoginViewModel(
            authenticationRepository = TestAuthenticationRepository(
                shouldLoginSucceed = shouldLoginSucceed,
                shouldUserBeLoggedIn = shouldUserBeLoggedIn
            ),
            navCoordinator = NavCoordinator(),
            resources = TestResourceProvider()
        ) {
        private val _events = MutableSharedFlow<UiEvent>()
        override val events = _events.asSharedFlow()

        // helpers to emit events from the test
        suspend fun emitLoading() {
            _events.emit(UiEvent.Loading)
        }

        suspend fun emitError(message: String) {
            _events.emit(UiEvent.ShowError(message))
        }

        // Keep default implementations for onLoginClicked/onBack or override if needed.
    }

    @Test
    fun loginScreen_basicInteraction_showsLoadingAndError() {
        val vm = TestLoginViewModel(shouldLoginSucceed = true, shouldUserBeLoggedIn = false)

        composeRule.setContent {
            LoginScreen(viewModel = vm)
        }

        // Advance the main clock well beyond the entrance animation so AnimatedVisibility content is visible.
        composeRule.mainClock.advanceTimeBy(1500L)


        val emailTextFiled = composeRule.onNodeWithTag(TestTags.LOGIN_EMAIL_EDIT_TEXT)
        val passwordTextFiled = composeRule.onNodeWithTag(TestTags.LOGIN_PASSWORD_EDIT_TEXT)

        emailTextFiled.performTextInput("test@example.com")
        passwordTextFiled.performTextInput("secretpw")


        val loginButtonNode = composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON)
        loginButtonNode.performClick()
        val loginButtonLabel = composeRule.onNodeWithTag(TestTags.LOGIN_BUTTON_LABEL, useUnmergedTree = true)
        loginButtonLabel.assertIsDisplayed()

        // Simulate the ViewModel signalling loading and assert a progress indicator appears.
        runBlocking {
            launch(Dispatchers.Main) { vm.emitLoading() }
        }
        // advance a bit so Compose can show the crossfaded progress indicator
        composeRule.mainClock.advanceTimeBy(200L)

        loginButtonNode.assertIsDisplayed()

        loginButtonNode.assertIsDisplayed()
        loginButtonLabel.assertIsNotDisplayed()
        val loadingIndicator = composeRule.onNodeWithTag(TestTags.LOGIN_LOADING_INDICATOR)
        loadingIndicator.assertIsDisplayed()

        // Simulate an error from the ViewModel and ensure snackbar shows
        val testMessage = "Invalid credentials"
        runBlocking {
            launch(Dispatchers.Main) { vm.emitError(testMessage) }
        }
        composeRule.mainClock.advanceTimeBy(500L)
         val test = loginButtonNode.toString()

        // Snackbar shows the error text
        composeRule.onNodeWithText(testMessage).assertIsDisplayed()
        loadingIndicator.assertIsNotDisplayed()
        loginButtonNode.assertIsDisplayed()
        loginButtonLabel.assertIsDisplayed()
    }
}