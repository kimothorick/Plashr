package com.kimothorick.plashr

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.kimothorick.plashr.data.remote.UnsplashAPI
import com.kimothorick.plashr.navgraphs.MainNavigation
import com.kimothorick.plashr.profile.domain.ProfileViewModel
import com.kimothorick.plashr.settings.domain.SettingsViewModel
import com.kimothorick.plashr.ui.theme.PlashrTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject

/**
 * The main activity of the application.
 *
 * This activity is responsible for setting up the main navigation and handling the app theme.
 * It utilizes Hilt for dependency injection and Jetpack Compose for UI.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var unsplashAPI: UnsplashAPI
    private val mainViewModel: MainViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var authService: AuthorizationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        authService = AuthorizationService(this)
        setContent {
            val navController = rememberNavController()

            var selectedTheme by remember {
                mutableStateOf("")
            }
            // Collect the app theme setting and update the selectedTheme state.
            LaunchedEffect(Unit) {
                settingsViewModel.appTheme.collect {theme ->
                    selectedTheme = theme
                }
            }

            // Apply the selected theme to the PlashrTheme composable
            PlashrTheme(
                darkTheme = when (selectedTheme) {
                    "Light" -> false
                    "Dark" -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                // Set up the main navigation graph.
                MainNavigation(
                    navController = navController,
                    settingsViewModel = settingsViewModel,
                    profileViewModel = profileViewModel,
                    viewModel = mainViewModel,
                    context = this,
                    mainActivity = this
                )

            }
        }
    }

    /**
     * Handles the authorization response from the Unsplash authentication flow.
     *
     * This property is registered for activity results and processes the authorization response received from Unsplash.
     * It handles both successful and failed authorization attempts, updating the UI and user data accordingly.
     */
    private val getAuthResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val exception = AuthorizationException.fromIntent(it.data!!)
                val result = AuthorizationResponse.fromIntent(it.data!!)

                mainViewModel.startLogin()

                val secret = ClientSecretPost(BuildConfig.client_secret)
                if (result != null) {
                    //Handle successful authorization code request here
                    val tokenRequest = result.createTokenExchangeRequest()

                    authService.performTokenRequest(
                        tokenRequest, secret
                    ) {response, ex ->
                        when {
                            response != null -> {
                                // Handle successful token exchange
                                response.accessToken?.let {accessToken ->
                                    profileViewModel.setAccessToken(
                                        accessToken = accessToken
                                    )
                                }

                                val userID = response.additionalParameters.getValue("user_id")
                                val username = response.additionalParameters.getValue("username")

                                profileViewModel.setUserId(userID)
                                profileViewModel.setUsername(username)
                                lifecycleScope.launch {
                                    getLoggedUserProfile(username)
                                }
                            }

                            ex != null -> {
                                mainViewModel.loginFailed()
                            }
                        }
                    }
                } else {
                    // Handle authorization code fetch error
                    mainViewModel.loginFailed()
                }
            }
        }

    /**
     * Initiates the Unsplash authentication flow.
     *
     * This function creates an authorization request and launches an intent to start the authentication processwith Unsplash.
     */
    fun unsplashAuth() {
        val authConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://unsplash.com/oauth/authorize"),
            Uri.parse("https://unsplash.com/oauth/token")
        )

        val request = AuthorizationRequest.Builder(
            authConfig, // configuration
            BuildConfig.client_id, // clientId
            ResponseTypeValues.CODE, // responseType
            Uri.parse(BuildConfig.redirect_uri) // redirectUri
        ).setScopes(
            "public",
            "read_user",
            "write_user",
            "read_photos",
            "write_photos",
            "write_likes",
            "write_followers",
            "read_collections",
            "write_collections"
        ).build()

        val intent = authService.getAuthorizationRequestIntent(request)
        getAuthResponse.launch(intent)
    }

    /**
     * Retrieves the logged user's public profile from the Unsplash API and stores the details
     * in the profile data store.
     *
     * This function makes a network request to fetch the user's
     * public profile using the provided `username`. If the request is successful, it extracts
     * relevant details from the response and calls `addUserDetails` in the `profileViewModel`
     * to store them. It then checks if all required fields are populated and updates
     * the login state accordingly.
     *
     * @param username The username of the logged-in user.
     */
    private suspend fun getLoggedUserProfile(username: String) {
        val response = unsplashAPI.getLoggedUserPublicProfile(username)
        if (response.isSuccessful) {
            profileViewModel.addUserDetails(
                userID = response.body()!!.id,
                username = response.body()!!.username,
                firstName = response.body()!!.first_name,
                lastName = response.body()!!.last_name,
                profilePictureUrl = response.body()!!.profile_image.large
            )

            profileViewModel.areAllFieldsPopulated.collect {allFieldsPopulated ->
                if (allFieldsPopulated) {
                    mainViewModel.loginSuccessful()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authService.dispose()
    }

}