package com.kimothorick.plashr.di

import com.kimothorick.plashr.BuildConfig
import com.kimothorick.plashr.profile.domain.ProfileDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An OkHttp interceptor that adds the appropriate authorization header to Unsplash API requests.
 *
 * If the user is logged in, it adds the `Authorization` header with the `Bearer` token.
 * Otherwise, it adds the `Authorization` header with the `Client-ID` for unauthenticated access.
 *
 * @param profileDataStore The data store used to access user profile information, including the
 * access token and login status.
 * */
class AuthenticationInterceptor @Inject constructor(
    private val profileDataStore: ProfileDataStore,
) : Interceptor {

    /**
     * Intercepts an OkHttp request and adds the necessary authorization header.
     *
     * @param chain The interceptor chain.
     * @return The modified response.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Check if the user is logged in (efficiently using runBlocking)
        val isLoggedIn = runBlocking {profileDataStore.isAppAuthorized.first()}

        if (isLoggedIn) {
            val token = runBlocking {profileDataStore.accessTokenFlow.first()}
            requestBuilder.addHeader("Authorization", "Bearer $token")
        } else {
            requestBuilder.addHeader("Authorization", "Client-ID ${BuildConfig.client_id}")
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}