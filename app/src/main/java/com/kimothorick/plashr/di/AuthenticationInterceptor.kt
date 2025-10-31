package com.kimothorick.plashr.di

import com.kimothorick.plashr.BuildConfig
import com.kimothorick.plashr.profile.domain.ProfileDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An [Interceptor] that adds the appropriate `Authorization` header to outgoing HTTP requests.
 *
 * This interceptor checks the user's authentication status via [ProfileDataStore].
 * - If the user is logged in (authorized), it adds a `Bearer` token to the request header.
 * - If the user is not logged in, it adds a `Client-ID` to the request header for public access to the API.
 *
 * This logic is executed synchronously for each request using `runBlocking` to ensure the header is
 * added before the request proceeds.
 *
 * @property profileDataStore The data store used to access the user's authentication state and access token.
 */
class AuthenticationInterceptor
    @Inject
    constructor(
        private val profileDataStore: ProfileDataStore,
    ) : Interceptor {
        private var isLoggedIn: Boolean? = null
        private var token: String? = null

        override fun intercept(
            chain: Interceptor.Chain,
        ): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()

            runBlocking {
                val authResult =
                    profileDataStore.isAppAuthorized.map { isAuthorized ->
                        if (isAuthorized) {
                            val token = profileDataStore.accessTokenFlow.first()
                            Pair(isAuthorized, token)
                        } else {
                            Pair(isAuthorized, null)
                        }
                    }.first()
                isLoggedIn = authResult.first
                token = authResult.second
            }

            if (isLoggedIn == true) {
                token?.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }
            } else {
                requestBuilder.addHeader("Authorization", "Client-ID ${BuildConfig.client_id}")
            }

            val newRequest = requestBuilder.build()
            return chain.proceed(newRequest)
        }
    }
