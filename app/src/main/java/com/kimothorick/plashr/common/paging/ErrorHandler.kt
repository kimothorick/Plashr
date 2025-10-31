package com.kimothorick.plashr.common.paging

import android.content.Context
import com.kimothorick.plashr.R
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A singleton utility class responsible for converting various [Throwable] instances
 * into user-friendly, localized error messages.
 *
 *
 * This class handles different types of exceptions, including network-related errors like
 * [HttpException], [SocketTimeoutException], and general [IOException], as well as
 * custom exceptions like [NullResponseBodyException]. For HTTP errors, it attempts to
 * parse the error body to provide more specific details from the API.
 *
 * @property context The application context, injected by Hilt, used to access string resources.
 */
@Singleton
class ErrorHandler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    /**
     * Converts a [Throwable] into a user-friendly, localized error message string.
     *
     * This function handles various types of exceptions commonly encountered during network
     * operations and provides specific, readable messages for each case.
     *
     * - For [HttpException], it parses the HTTP status code and the error body (if available)
     *   to generate a detailed message. It looks for a JSON object with an "errors" array
     *   in the response body to provide more context.
     * - For network-related issues like [SocketTimeoutException] and other [IOException],
     *   it returns predefined messages for timeouts and general network problems.
     * - For [NullResponseBodyException], it indicates that the server's response was empty when
     *   content was expected.
     * - For any other type of [Throwable], it provides a generic "unexpected error" message,
     *   including a snippet of the original error message for debugging purposes.
     *
     * @param error The [Throwable] instance to be processed.
     * @return A [String] containing the formatted, user-friendly error message.
     */
    fun getErrorMessage(
        error: Throwable,
    ): String {
        return when (error) {
            is NullResponseBodyException -> {
                return context.applicationContext.getString(R.string.null_response)
            }

            is HttpException -> {
                val response = error.response()
                val errorBody = response?.errorBody()?.string()
                var apiErrorDetails: String? = null

                if (!errorBody.isNullOrBlank()) {
                    try {
                        val jsonObject = JSONObject(errorBody)
                        if (jsonObject.has("errors")) {
                            val errorsArray = jsonObject.getJSONArray("errors")
                            val messages = mutableListOf<String>()
                            for (i in 0 until errorsArray.length()) {
                                messages.add(errorsArray.getString(i))
                            }
                            if (messages.isNotEmpty()) {
                                apiErrorDetails = messages.joinToString(", ")
                            }
                        }
                    } catch (_: Exception) {
                    }
                }

                fun formatMessage(
                    baseMessage: String,
                    details: String?,
                ): String {
                    return if (details != null) {
                        "$baseMessage\n$details"
                    } else {
                        baseMessage
                    }
                }

                when (error.code()) {
                    400 -> formatMessage(context.getString(R.string.error_bad_request), apiErrorDetails)
                    401 -> formatMessage(context.getString(R.string.error_unauthorized), apiErrorDetails)
                    403 -> formatMessage(context.getString(R.string.error_forbidden), apiErrorDetails)
                    404 -> formatMessage(context.getString(R.string.error_not_found), apiErrorDetails)
                    422 -> formatMessage(context.getString(R.string.error_unprocessable), apiErrorDetails)
                    500, 503 -> formatMessage(context.getString(R.string.error_server_error, error.code()), apiErrorDetails)
                    in 400..499 -> {
                        val baseMessagePrefix = context.getString(R.string.error_client_error, error.code())
                        if (apiErrorDetails != null) {
                            formatMessage(baseMessagePrefix, apiErrorDetails)
                        } else {
                            formatMessage(context.getString(R.string.error_client_check_input, error.code()), null)
                        }
                    }

                    in 500..599 -> {
                        val baseMessagePrefix = context.getString(R.string.error_server_error_retry, error.code())
                        if (apiErrorDetails != null) {
                            formatMessage(baseMessagePrefix, apiErrorDetails) + context.getString(R.string.error_try_again_later_suffix)
                        } else {
                            formatMessage(baseMessagePrefix, null)
                        }
                    }

                    else -> {
                        val baseMessagePrefix = context.getString(R.string.error_http_error, error.code())
                        if (apiErrorDetails != null) {
                            formatMessage(baseMessagePrefix, apiErrorDetails)
                        } else {
                            formatMessage(baseMessagePrefix, null)
                        }
                    }
                }
            }

            is SocketTimeoutException -> {
                context.getString(R.string.error_timeout)
            }

            is IOException -> {
                context.getString(R.string.error_network_problem)
            }

            else -> {
                val detail = error.message?.take(100) ?: context.getString(R.string.error_no_specific_details)
                context.getString(R.string.error_unexpected_with_details, detail)
            }
        }
    }
}
