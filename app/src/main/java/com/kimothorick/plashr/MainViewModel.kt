package com.kimothorick.plashr

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.kimothorick.plashr.profile.presentation.components.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * The main ViewModel for the application.
 *
 * This ViewModel manages the [NavHostController] and the visibility of the login bottom sheet.
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // Private mutable state flow for the NavHostController
    private val _navController = MutableStateFlow<NavHostController?>(null)

    // Public state flow for the NavHostController, accessible by other composables
    val navController: StateFlow<NavHostController?> = _navController.asStateFlow()

    /**
     * Sets the [NavHostController] for this ViewModel.
     *
     * @param controller The [NavHostController] to be set.
     */
    fun setNavController(controller: NavHostController) {
        _navController.value = controller
    }

    // Private mutable state flow for the visibility of the login bottom sheet
    val _showLoginBottomSheet = MutableStateFlow(false)

    // Public state flow for the visibility of the login bottom sheet
    val showLoginBottomSheet: StateFlow<Boolean> = _showLoginBottomSheet.asStateFlow()

    /**
     * Checks if the app is authorized and shows the login bottom sheet if not.
     *
     * @param isAppAuthorized A boolean flag indicating whether the app is authorized.
     */
    fun checkAndShowLoginBottomSheet(isAppAuthorized: Boolean) {
        if (!isAppAuthorized) {
            _showLoginBottomSheet.value = true
        }
    }

    // Private mutable state flow for the visibility of the login bottom sheet
    val _showManageAccountBottomSheet = MutableStateFlow(false)

    // Public state flow for the visibility of the login bottom sheet
    val showManageAccountBottomSheet: StateFlow<Boolean> =
        _showManageAccountBottomSheet.asStateFlow()

    /**
     * Checks if the app is authorized and shows the login bottom sheet if not.
     *
     * @param isAppAuthorized A boolean flag indicating whether the app is authorized.
     */
    fun showManageAccountBottomSheet() {
        _showLoginBottomSheet.value = true
    }

    /**
     * A private MutableStateFlow to hold the current login state.
     */
    private val _loginState = MutableStateFlow(LoginState.Initial)

    /**
     * A public StateFlow exposing the current login state.
     * This allows other composables to observe changes to the login state.
     */
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    /**
     * Sets the login state to [LoginState.Initial].
     * This is typically used to reset the login state before a new login attempt or after logout.
     */
    fun loginInitial() {
        _loginState.value = LoginState.Initial
    }

    /**
     * Sets the login state to [LoginState.LoggingIn].
     * This indicates that a login operation is in progress.
     */
    fun startLogin() {
        _loginState.value = LoginState.LoggingIn
    }

    /**
     * Sets the login state to [LoginState.LoginSuccess].
     * This signifies that the login attempt was successful.
     */
    fun loginSuccessful() {
        _loginState.value = LoginState.LoginSuccess
    }

    /**
     * Sets the login state to [LoginState.Initial].
     * This is used to reset the login state after a user logs out.
     */
    fun logout() {
        _loginState.value = LoginState.Initial
    }

    /**
     * Sets the login state to [LoginState.LoginFailed].
     * This indicates that the login attempt has failed.
     */
    fun loginFailed() {
        _loginState.value = LoginState.LoginFailed
    }



}