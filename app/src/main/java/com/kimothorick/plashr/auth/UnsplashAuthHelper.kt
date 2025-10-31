package com.kimothorick.plashr.auth

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.BuildConfig
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.profile.presentation.components.LoginState
import dagger.hilt.android.qualifiers.ApplicationContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A singleton helper class for managing the Unsplash OAuth 2.0 authorization flow.
 *
 * This class uses the AppAuth library to handle the complexities of the OAuth 2.0 protocol,
 * including creating the authorization request, handling the redirect, and exchanging the
 * authorization code for an access token.
 *
 * It is designed to be used as a singleton throughout the application, managed by Hilt.
 *
 * @property applicationContext The application context, injected by Hilt, used to initialize the [AuthorizationService].
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
@Singleton
class UnsplashAuthHelper
    @Inject
    constructor(
        @ApplicationContext private val applicationContext: Context,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        private var serviceInitialized = false

        private val authorizationService: AuthorizationService by lazy {
            serviceInitialized = true
            AuthorizationService(applicationContext)
        }
        private var authState: AuthState? = null

        private val scopes =
            arrayOf(
                "public",
                "read_user",
                "write_user",
                "read_photos",
                "write_photos",
                "write_likes",
                "write_followers",
                "read_collections",
                "write_collections",
            )

        fun createAuthIntent(): Intent {
            val authConfig =
                AuthorizationServiceConfiguration(
                    Constants.UnsplashLogin.AUTHORIZATION_URL.toUri(),
                    Constants.UnsplashLogin.TOKEN_URL.toUri(),
                )

            val authorizationRequest =
                AuthorizationRequest.Builder(
                    authConfig,
                    BuildConfig.client_id,
                    ResponseTypeValues.CODE,
                    BuildConfig.redirect_uri.toUri(),
                ).setScopes(
                    *scopes,
                ).build()

            return authorizationService.getAuthorizationRequestIntent(authorizationRequest)
        }

        fun handleAuthorizationResponse(
            data: Intent?,
            onSuccess: (accessToken: String, userId: String, username: String) -> Unit,
            onError: (loginState: LoginState) -> Unit,
        ) {
            val response = AuthorizationResponse.fromIntent(data ?: Intent())
            val ex = AuthorizationException.fromIntent(data ?: Intent())

            authState = AuthState(response, ex)

            if (response != null) {
                val clientAuthentication: ClientAuthentication =
                    ClientSecretPost(BuildConfig.client_secret)

                authorizationService.performTokenRequest(
                    response.createTokenExchangeRequest(),
                    clientAuthentication,
                ) { tokenResponse, exception ->
                    if (tokenResponse != null) {
                        authState!!.update(tokenResponse, exception)
                        val accessToken = tokenResponse.accessToken
                        val userIdFromParams = tokenResponse.additionalParameters["user_id"]
                        val usernameFromParams = tokenResponse.additionalParameters["username"]

                        if (accessToken != null && userIdFromParams != null && usernameFromParams != null) {
                            onSuccess(accessToken, userIdFromParams, usernameFromParams)
                        } else {
                            val error = IllegalStateException("Token response is missing required parameters.")
                            crashlytics.recordException(error)
                            onError(LoginState.FAILED)
                        }
                    } else {
                        val error = exception ?: IllegalStateException("Token response is null")
                        crashlytics.recordException(error)
                        onError(LoginState.FAILED)
                    }
                }
            } else {
                val error = ex ?: IllegalStateException("Authorization response is null")
                crashlytics.recordException(error)
                onError(LoginState.FAILED)
            }
        }

        fun onDestroy() {
            // Ensure service is disposed only if it was initialized
            if (serviceInitialized) {
                authorizationService.dispose()
            }
        }
    }
