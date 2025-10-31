package com.kimothorick.plashr.auth

import com.kimothorick.plashr.profile.domain.ProfileDataStore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Repository for handling authentication-related data and logic.
 *
 * This class serves as a single source of truth for the application's authorization state.
 * It observes the access token from the [ProfileDataStore] to determine if the user is
 * currently authorized.
 *
 * @param profileDataStore The data store that persists user profile information, including the access token.
 */
@Singleton
class AuthRepository
    @Inject constructor(
        profileDataStore: ProfileDataStore,
    ) {
        val isAppAuthorized: StateFlow<Boolean> = profileDataStore.accessTokenFlow.map { accessToken ->
            accessToken.isNotEmpty()
        }.stateIn(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false,
        )
    }
