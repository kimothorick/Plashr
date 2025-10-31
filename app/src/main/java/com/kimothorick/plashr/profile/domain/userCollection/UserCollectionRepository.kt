package com.kimothorick.plashr.profile.domain.userCollection

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants
import com.kimothorick.plashr.data.models.collection.CreateCollection
import com.kimothorick.plashr.data.models.user.UserCollection
import com.kimothorick.plashr.data.remote.CollectionsDataService
import com.kimothorick.plashr.data.remote.UserDataService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A repository for managing user collections.
 *
 * This class abstracts the data sources for user collections, providing a clean API
 * for the domain and UI layers to interact with. It handles operations such as fetching a
 * user's list of collections with pagination and creating new collections.
 *
 * @property userDataService Service for accessing user-related data from the API.
 * @property collectionsDataService Service for collection-specific actions like creation.
 * @property crashlytics Service for logging non-fatal exceptions.
 */
class UserCollectionRepository
    @Inject constructor(
        private val userDataService: UserDataService,
        private val collectionsDataService: CollectionsDataService,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        /**
         * Retrieves a paginated flow of collections for a specific user.
         *
         * This function uses the Paging 3 library to create a [Flow] of [PagingData] that
         * can be collected by the UI to display a user's collections in a list. The data
         * is fetched from the network using [UserCollectionPagingSource].
         *
         * @param username The username of the user whose collections are to be fetched.
         * @return A [Flow] of [PagingData] containing [UserCollection] items.
         */
        fun getCollections(
            username: String,
        ): Flow<PagingData<UserCollection>> =
            Pager(
                config = PagingConfig(
                    pageSize = Constants.Paging.NETWORK_PAGE_SIZE,
                    prefetchDistance = Constants.Paging.NETWORK_PAGE_SIZE / 2,
                    enablePlaceholders = false,
                    initialLoadSize = Constants.Paging.NETWORK_PAGE_SIZE,
                ),
                pagingSourceFactory = { UserCollectionPagingSource(username, userDataService, crashlytics) },
            ).flow

        /**
         * Creates a new photo collection.
         *
         * This function makes a network request to create a new collection for the authenticated user.
         *
         * @param title The title of the collection.
         * @param description A short description of the collection (optional).
         * @param isPrivate Whether to make the collection private. Defaults to false (optional).
         * @return A [Result] object containing the newly created [CreateCollection] details.
         */
        suspend fun createCollection(
            title: String,
            description: String?,
            isPrivate: Boolean?,
        ): Result<CreateCollection> {
            return try {
                val response = collectionsDataService.createCollection(
                    title = title,
                    description = description,
                    isPrivate = isPrivate ?: false,
                )
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val exception = Exception(response.message())
                    crashlytics.recordException(exception)
                    Result.failure(exception)
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                Result.failure(e)
            }
        }
    }
