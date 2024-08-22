package com.kimothorick.plashr

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * The main ViewModel for the application.
 *
 * This ViewModel manages the [NavHostController] and the visibility of the loginbottom sheet.
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

    // Public state flowfor the visibility of the login bottom sheet
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
}