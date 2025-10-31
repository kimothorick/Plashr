package com.kimothorick.plashr.utils

import android.util.Log
import com.kimothorick.plashr.BuildConfig

/**
 * A utility object for logging messages with enhanced context.
 *
 * This logger automatically includes the class name, method name, file name, and line number
 * from where the log was called, making debugging easier. The log output is only generated
 * in `DEBUG` builds, as determined by `BuildConfig.DEBUG`.
 *
 * Usage example:
 * ```
 * LogUtil.log("User ID", 12345)
 * LogUtil.log("API Response", responseBody, LogUtil.LogType.INFO)
 * LogUtil.log("Error", exception.message, LogUtil.LogType.ERROR)
 * ```
 * The log output will be formatted like: `(MyActivity.kt:42) ➔ User ID (Int) = 12345`
 */
object LogUtil {
    /**
     * Represents the different levels of logging available.
     * This is used to categorize log messages based on their severity.
     */
    enum class LogType { DEBUG, INFO, WARN, ERROR }

    /**
     * Logs a key-value pair with additional contextual information.
     * This function automatically includes the class name as the tag, and the file name,
     * line number, and data type of the value in the log message.
     * The log message format is: `(FileName:LineNumber) ➔ keyName (DataType) = value`.
     *
     * This log will only be printed in `DEBUG` builds (controlled by `BuildConfig.DEBUG`).
     *
     * Example usage:
     * ```kotlin
     * LogUtil.log("User ID", 12345)
     * LogUtil.log("API Response", responseBody, LogType.INFO)
     * LogUtil.log("Error occurred", exception, LogType.ERROR)
     * ```
     *
     * @param keyName A descriptive name for the value being logged.
     * @param value The actual value to log. Can be any type, including null.
     * @param type The type of log (e.g., DEBUG, INFO, WARN, ERROR). Defaults to [LogType.DEBUG].
     */
    fun log(
        keyName: String,
        value: Any?,
        type: LogType = LogType.DEBUG,
    ) {
        if (!BuildConfig.DEBUG) return
        val stackTrace = Throwable().stackTrace[1]
        val tag = stackTrace.className.substringAfterLast(".") // Auto Class Name
        val fileName = stackTrace.fileName // File Name for clickable link
        stackTrace.methodName // Auto Method Name
        val lineNumber = stackTrace.lineNumber // Auto Line Number

        val dataType = value?.let { it::class.simpleName } ?: "null"

        val logMessage = "($fileName:$lineNumber) ➔ $keyName ($dataType) = $value"

        when (type) {
            LogType.DEBUG -> Log.d(tag, logMessage)
            LogType.INFO -> Log.i(tag, logMessage)
            LogType.WARN -> Log.w(tag, logMessage)
            LogType.ERROR -> Log.e(tag, logMessage)
        }
    }
}
