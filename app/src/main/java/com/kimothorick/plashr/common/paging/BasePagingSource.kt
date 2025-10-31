package com.kimothorick.plashr.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kimothorick.plashr.common.Constants.Paging.UNSPLASH_STARTING_PAGE_INDEX
import com.kimothorick.plashr.utils.LogUtil
import retrofit2.HttpException
import java.io.IOException

/**
 * An abstract base class for implementing a [PagingSource] that pages by integer keys.
 * This class simplifies the creation of paging sources by handling common logic such as:
 * - Calculating the current page number.
 * - Calculating the next and previous keys.
 * - Handling common network exceptions ([IOException], [HttpException], [NullResponseBodyException]).
 * - Logging the paging process and any errors that occur.
 *
 * Subclasses must implement [loadData] to define the specific API call for fetching a page of data
 * and provide a [logKeyName] for logging purposes.
 *
 * @param T The type of items in the list. Must be non-nullable (`Any`).
 * @property crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
abstract class BasePagingSource<T : Any>(
    private val crashlytics: FirebaseCrashlytics,
) : PagingSource<Int, T>() {

    /**
     * A unique key name used for logging purposes. This helps in identifying
     * the source of logs when multiple PagingSource implementations are used.
     * Each subclass should provide a distinct name.
     */
    abstract val logKeyName: String

    /**
     * Abstract function to be implemented by subclasses to fetch the actual data.
     * This function is called by the `load` method of the PagingSource.
     *
     * @param params Parameters for the load operation, including the key for the page to be loaded
     * and the requested load size.
     * @return A list of data items of type [T].
     * @throws IOException for network errors.
     * @throws HttpException for non-2xx HTTP responses.
     * @throws NullResponseBodyException if the response body is unexpectedly null.
     */
    abstract suspend fun loadData(
        params: LoadParams<Int>,
    ): List<T>

    /**
     * Loads a page of data for the PagingSource.
     *
     * This function is called by the Paging library to fetch a chunk of data. It determines the
     * current page number, calls the abstract `loadData` function to get the actual data, and
     * then wraps the result in a `LoadResult.Page` object. It also handles common network
     * exceptions like `IOException` and `HttpException`, returning a `LoadResult.Error` in
     * case of failure.
     *
     * @param params Parameters for the load request, including the key for the page to be loaded
     *               and the requested load size. The key is an `Int` representing the page number.
     * @return A [LoadResult] object, which is either a [LoadResult.Page] containing the fetched
     *         data and keys for the previous/next pages, or a [LoadResult.Error] if an error occurred.
     */
    override suspend fun load(
        params: LoadParams<Int>,
    ): LoadResult<Int, T> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            LogUtil.log(
                keyName = logKeyName,
                value = "Fetching page: $page, loadSize: ${params.loadSize}",
                type = LogUtil.LogType.DEBUG,
            )

            val responseData = loadData(params)

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1,
            )
        } catch (exception: IOException) {
            LogUtil.log(
                keyName = logKeyName,
                value = "IOException on page $page: ${exception.message}",
                type = LogUtil.LogType.ERROR,
            )
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LogUtil.log(
                keyName = logKeyName,
                value = "HttpException on page $page: ${exception.code()} - ${exception.message()}",
                type = LogUtil.LogType.ERROR,
            )
            if (exception.code() in 500..599) {
                logExceptionToCrashlytics(page, exception)
            }
            LoadResult.Error(exception)
        } catch (exception: NullResponseBodyException) {
            LogUtil.log(
                keyName = logKeyName,
                value = "Null response body on page $page",
                type = LogUtil.LogType.ERROR,
            )
            logExceptionToCrashlytics(page, exception)
            LoadResult.Error(exception)
        }
    }

    /**
     * Provides the key for the page to be loaded when the data is refreshed.
     *
     * This function is called when the Paging library needs to refresh the data, for example,
     * after a configuration change or when[androidx.paging.PagingDataAdapter.refresh] is called.
     *
     * The logic is to find the closest page to the last accessed position (`anchorPosition`).
     * If a previous page is available, its key is incremented by one to load the page
     * containing the anchor item. If not, the next page's key is decremented by one.
     * This ensures that the user's viewport is preserved across refreshes.
     *
     * @param state The current state of the paging data, including loaded pages and the anchor position.
     * @return The key for the page to load on refresh, or `null` if the anchor position is not available.
     */
    override fun getRefreshKey(
        state: PagingState<Int, T>,
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    /**
     * Logs a non-fatal exception to Firebase Crashlytics with additional context.
     *
     * This helper function records exceptions that occur during the paging load process,
     * particularly for server-side errors (HTTP 5xx) or unexpected null responses.
     * It attaches custom keys to the Crashlytics report to provide context, including
     * which `PagingSource` implementation failed (`logKeyName`) and at which specific
     * page number the failure occurred. This helps in diagnosing and debugging
     * backend issues or unexpected API behavior from the client-side logs.
     *
     * @param page The page number that failed to load.
     * @param exception The exception that was caught and is being reported.
     */
    private fun logExceptionToCrashlytics(
        page: Int,
        exception: Exception,
    ) {
        crashlytics.setCustomKey("paging_source_key", logKeyName)
        crashlytics.setCustomKey("paging_page_failed", page)
        crashlytics.recordException(exception)
    }
}
