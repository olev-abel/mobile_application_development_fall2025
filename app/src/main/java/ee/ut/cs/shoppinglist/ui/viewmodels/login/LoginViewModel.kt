package ee.ut.cs.shoppinglist.ui.viewmodels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ee.ut.cs.shoppinglist.NavCoordinator
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.common.ResourceProvider
import ee.ut.cs.shoppinglist.data.remote.model.NetworkResult
import ee.ut.cs.shoppinglist.domain.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


private val PASSWORD_MIN_LENGTH = 6

open class LoginViewModel(
    private val navCoordinator: NavCoordinator,
    private val authenticationRepository: AuthenticationRepository,
    private val resources: ResourceProvider
) : ViewModel() {
    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data object Loading : UiEvent()
        data object Success: UiEvent()
    }

    private val _events = MutableSharedFlow<UiEvent>(replay = 0)
    open val events = _events.asSharedFlow()

    fun onLoginClicked(email: String, password: String) {
        startLoading()
        if (!isInputValid(email, password)) return
        viewModelScope.launch {
            val res = authenticationRepository.login(email, password)
            when (res) {
                is NetworkResult.Error -> _events.emit(
                    UiEvent.ShowError(
                        res.message
                    )
                )

                NetworkResult.Loading -> _events.emit(UiEvent.Loading)
                is NetworkResult.Success<*> -> {
                    _events.emit(UiEvent.Success)
                    navCoordinator.toListScreen()
                }
            }
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        if (!isValidEmail(email)) {
            emitError(resources.getString(R.string.error_enter_valid_email))
            return false
        }
        if (!isValidPassword(password)) {
            emitError(
                resources.getString(
                    R.string.error_password_too_short,
                    PASSWORD_MIN_LENGTH
                )
            )
            return false
        }
        return true
    }

    private fun isValidEmail(text: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()

    private fun isValidPassword(text: String): Boolean =
        text.length >= PASSWORD_MIN_LENGTH

    private fun startLoading() {
        viewModelScope.launch {
            _events.emit(UiEvent.Loading)
        }
    }

    private fun emitError(message: String) {
        viewModelScope.launch {
            _events.emit(UiEvent.ShowError(message))
        }
    }
}