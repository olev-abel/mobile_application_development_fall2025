package ee.ut.cs.shoppinglist.ui.viewmodels

import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.Screen
import ee.ut.cs.shoppinglist.common.TestResourceProvider
import ee.ut.cs.shoppinglist.data.TestAuthenticationRepository
import ee.ut.cs.shoppinglist.data.TestNavCoordinator
import ee.ut.cs.shoppinglist.ui.viewmodels.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTests {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(
        shouldLoginSucceed: Boolean = true,
        shouldUserBeLoggedIn: Boolean = false,
        testNavCoordinator: TestNavCoordinator = TestNavCoordinator()
    ): LoginViewModel {
        return LoginViewModel(
            navCoordinator = testNavCoordinator,
            authenticationRepository = TestAuthenticationRepository(
                shouldLoginSucceed = shouldLoginSucceed,
                shouldUserBeLoggedIn = shouldUserBeLoggedIn
            ),
            resources = TestResourceProvider()
        )
    }

    @Test
    fun validInputs_navigatesToListScreen() = runTest {
        val testNav = TestNavCoordinator()
        val vm = createViewModel(testNavCoordinator = testNav)
        vm.onLoginClicked("test@example.com", "securePassword123")
        testScope.advanceUntilIdle()

        assertEquals(Screen.ListScreen.route, testNav.lastRoute)
    }

    @Test
    fun invalidEmail_emitsLoadingThenEmailError() = runTest {
        val vm = createViewModel()
        val expected = TestResourceProvider().getString(R.string.error_enter_valid_email)

        val eventsDeferred = async {
            vm.events.take(2).toList()
        }

        vm.onLoginClicked("not-a-valid-email", "validPassword123")
        // allow launched coroutines to run
        testScope.advanceUntilIdle()

        val events = eventsDeferred.await()
        assertEquals(2, events.size)
        assertTrue(events[0] is LoginViewModel.UiEvent.Loading)
        val second = events[1]
        assertTrue(second is LoginViewModel.UiEvent.ShowError)
        assertEquals(expected, (second as LoginViewModel.UiEvent.ShowError).message)
    }

    @Test
    fun shortPassword_emitsLoadingThenPasswordError() = runTest {
        val vm = createViewModel()
        val expected = TestResourceProvider().getString(R.string.error_password_too_short, 6)

        val eventsDeferred = async {
            vm.events.take(2).toList()
        }

        vm.onLoginClicked("test@example.com", "123")
        testScope.advanceUntilIdle()

        val events = eventsDeferred.await()
        assertEquals(2, events.size)
        assertTrue(events[0] is LoginViewModel.UiEvent.Loading)
        val second = events[1]
        assertTrue(second is LoginViewModel.UiEvent.ShowError)
        assertEquals(expected, (second as LoginViewModel.UiEvent.ShowError).message)
    }
}