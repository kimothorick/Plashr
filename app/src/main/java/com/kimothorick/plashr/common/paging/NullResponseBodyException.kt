package com.kimothorick.plashr.common.paging

/**
 * Custom exception thrown when a network response body is unexpectedly null.
 * This is used to differentiate from other types of exceptions (like HttpException or IOException)
 * and provide a specific, user-friendly error message.
 *
 * @param debugMessage A descriptive message for debugging purposes, not intended for the user.
 */
class NullResponseBodyException(
    debugMessage: String = "Response body was null when a non-null body was expected.",
) : Exception(debugMessage)
