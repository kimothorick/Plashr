package com.kimothorick.plashr.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

private const val TAG = "DownloadHandler" // Consistent tag

/**
 * Manages file downloads using Android's [DownloadManager].
 *
 * This class handles the entire download lifecycle, from enqueuing a new download
 * to listening for completion status via a [BroadcastReceiver]. It saves files
 * to a specific application folder within the public "Downloads" directory.
 * Callbacks are used to communicate the download start, success, or failure
 * back to the caller.
 *
 * This class is designed to be injected as a singleton or within a specific scope
 * where its lifecycle can be managed.
 *
 * It is crucial to call [cleanup] when the handler is no longer needed (e.g., in `onDestroy`
 * of a ViewModel or component) to unregister the [BroadcastReceiver] and prevent memory leaks.
 *
 * @param context The application context, used to access system services like [DownloadManager].
 * @param crashlytics The Firebase Crashlytics instance for logging exceptions.
 */
class DownloadHandler
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val crashlytics: FirebaseCrashlytics,
    ) {
        // Map to store download callbacks
        private val downloadCallbacks = mutableMapOf<Long, (Boolean, String?) -> Unit>()

        private val downloadManager by lazy {
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }

        private val downloadReceiver = DownloadBroadcastReceiver()

        /**
         * A private inner `BroadcastReceiver` responsible for listening to system-wide download completion events.
         *
         * This receiver is registered to listen specifically for the `DownloadManager.ACTION_DOWNLOAD_COMPLETE`
         * intent action. When a download completes, it extracts the download ID from the intent and
         * calls `checkDownloadStatus` to process the result and invoke the appropriate callback.
         */
        private inner class DownloadBroadcastReceiver : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent,
            ) {
                LogUtil.log(TAG, "BroadcastReceiver.onReceive called with action: ${intent.action}", LogUtil.LogType.DEBUG)

                if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                    val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    LogUtil.log(TAG, "Download complete broadcast received for ID: $downloadId", LogUtil.LogType.INFO)

                    if (downloadId != -1L) {
                        checkDownloadStatus(downloadId)
                    }
                }
            }
        }

        // Initialize and register the receiver
        init {
            // Register the BroadcastReceiver to listen for download completions
            val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            ContextCompat.registerReceiver(
                context,
                downloadReceiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED,
            )
            LogUtil.log(TAG, "BroadcastReceiver registered for download completions", LogUtil.LogType.DEBUG)
        }

        /**
         * Queries the [DownloadManager] to determine the final status of a completed download.
         *
         * This function is typically called by the [DownloadBroadcastReceiver] when it receives
         * an [DownloadManager.ACTION_DOWNLOAD_COMPLETE] intent. It checks if the download
         * was successful or if it failed, and retrieves a reason code in case of failure.
         *
         * Based on the outcome, it invokes the corresponding callback stored in the
         * [downloadCallbacks] map and then removes the callback to prevent memory leaks.
         *
         * @param downloadId The unique ID of the download to check, obtained from the
         *   [DownloadManager].
         */
        private fun checkDownloadStatus(
            downloadId: Long,
        ) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            try {
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status = cursor.getInt(columnIndex)

                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            LogUtil.log(
                                TAG,
                                "Download ID: $downloadId completed successfully",
                                LogUtil.LogType.INFO,
                            )
                            downloadCallbacks[downloadId]?.invoke(true, null)
                        }

                        DownloadManager.STATUS_FAILED -> {
                            val reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                            val reason = cursor.getInt(reasonIndex)
                            val errorMessage = "Download failed with reason code: $reason"
                            LogUtil.log(TAG, errorMessage, LogUtil.LogType.ERROR)
                            val exception = IOException("Download failed: $errorMessage")
                            crashlytics.recordException(exception)
                            downloadCallbacks[downloadId]?.invoke(false, errorMessage)
                        }
                    }

                    // Clean up the callback after handling
                    downloadCallbacks.remove(downloadId)
                }
                cursor.close()
            } catch (e: Exception) {
                LogUtil.log(TAG, "Error checking download status: ${e.message}", LogUtil.LogType.ERROR)
                crashlytics.recordException(e)
                downloadCallbacks[downloadId]?.invoke(false, "Error checking status: ${e.message}")
                downloadCallbacks.remove(downloadId)
            }
        }

        /**
         * Unregisters the `BroadcastReceiver` to prevent memory leaks.
         * This method should be called when the `DownloadHandler` is no longer needed,
         * for example, in the `onDestroy` lifecycle method of an Activity or Fragment
         * where this handler is being used. It safely attempts to unregister the
         * receiver and logs any errors that might occur.
         */
        fun cleanup() {
            try {
                context.unregisterReceiver(downloadReceiver)
                LogUtil.log(TAG, "BroadcastReceiver unregistered", LogUtil.LogType.DEBUG)
            } catch (e: Exception) {
                LogUtil.log(TAG, "Error unregistering receiver: ${e.message}", LogUtil.LogType.ERROR)
                crashlytics.recordException(e)
            }
        }

        /**
         * Initiates a file download using Android's [DownloadManager].
         *
         * This function handles the creation of a download request, specifies the destination directory,
         * and enqueues the download. It creates a dedicated "Plashr" folder inside the public
         * "Downloads" directory if it doesn't already exist. The function also prevents re-downloading
         * if a file with the same name already exists at the destination.
         *
         * Callbacks are used to communicate the state of the download back to the caller. The completion
         * callback is stored and later invoked by a [BroadcastReceiver] upon receiving the
         * `ACTION_DOWNLOAD_COMPLETE` intent.
         *
         * @param fileUrl The URL of the file to be downloaded.
         * @param fileName The desired name for the downloaded file.
         * @param onDownloadStarted A callback function that is invoked immediately after the download
         * is successfully enqueued. It receives the unique download ID (`Long`) as an argument.
         * @param onDownloadComplete A callback function that is invoked when the download finishes
         * (either successfully or with an error). It receives a `Boolean` indicating success and an
         * optional `String` with an error message if the download failed or was pre-emptively stopped
         * (e.g., file already exists).
         */
        fun startDownload(
            fileUrl: String,
            fileName: String,
            onDownloadStarted: (Long) -> Unit,
            onDownloadComplete: (Boolean, String?) -> Unit,
        ) {
            LogUtil.log(
                TAG,
                "startDownload: START - URL = $fileUrl, fileName = $fileName",
                LogUtil.LogType.DEBUG,
            )

            try {
                val request =
                    DownloadManager.Request(fileUrl.toUri()).setTitle(fileName)
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                val appFolderName = "Plashr"
                val destinationDir =
                    File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        appFolderName,
                    )

                LogUtil.log(TAG, "startDownload: appFolderName = $appFolderName", LogUtil.LogType.DEBUG)
                LogUtil.log(
                    TAG,
                    "startDownload: destinationDir = ${destinationDir.absolutePath}",
                    LogUtil.LogType.DEBUG,
                )

                if (!destinationDir.exists()) {
                    LogUtil.log(
                        TAG,
                        "startDownload: Directory does not exist, creating...",
                        LogUtil.LogType.DEBUG,
                    )
                    if (!destinationDir.mkdirs()) {
                        val errorMessage = "Failed to create directory = ${destinationDir.absolutePath}"
                        LogUtil.log(
                            TAG,
                            "startDownload: $errorMessage",
                            LogUtil.LogType.ERROR,
                        )
                        val exception = IOException(errorMessage)
                        crashlytics.recordException(exception)
                        onDownloadComplete(false, errorMessage)
                        return // Stop download if directory creation fails
                    }
                    LogUtil.log(
                        TAG,
                        "startDownload: Directory created = ${destinationDir.absolutePath}",
                        LogUtil.LogType.DEBUG,
                    )
                } else {
                    LogUtil.log(TAG, "startDownload: Directory already exists", LogUtil.LogType.DEBUG)
                }

                val destinationFile = File(destinationDir, fileName)
                if (destinationFile.exists()) {
                    LogUtil.log(
                        TAG,
                        "startDownload: File already exists, not downloading",
                        LogUtil.LogType.WARN,
                    )
                    // Instead of just returning, inform the caller
                    onDownloadComplete(false, "File already exists")
                    return // Stop the download
                }

                val destinationUri = Uri.fromFile(destinationFile)
                request.setDestinationUri(destinationUri)
                LogUtil.log(
                    TAG,
                    "startDownload: Destination URI = $destinationUri",
                    LogUtil.LogType.DEBUG,
                )

                val downloadManager =
                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                LogUtil.log(
                    TAG,
                    "startDownload: DownloadManager = $downloadManager",
                    LogUtil.LogType.DEBUG,
                )

                val downloadId = downloadManager.enqueue(request)

                // Store the callback for this download ID
                downloadCallbacks[downloadId] = onDownloadComplete
                LogUtil.log(
                    TAG,
                    "startDownload: Download enqueued, ID = $downloadId",
                    LogUtil.LogType.INFO,
                )
                onDownloadStarted(downloadId)
            } catch (exception: IllegalArgumentException) {
                LogUtil.log(
                    TAG,
                    "startDownload: IllegalArgumentException - ${exception.message}",
                    LogUtil.LogType.ERROR,
                )
                crashlytics.recordException(exception)
                onDownloadComplete(false, "Error: ${exception.message}")
            } catch (exception: SecurityException) {
                LogUtil.log(
                    TAG,
                    "startDownload: SecurityException - ${exception.message}",
                    LogUtil.LogType.ERROR,
                )
                crashlytics.recordException(exception)
                onDownloadComplete(false, "Error: ${exception.message}")
            } catch (exception: Exception) {
                LogUtil.log(TAG, "startDownload: Exception - ${exception.message}", LogUtil.LogType.ERROR)
                crashlytics.recordException(exception)
                onDownloadComplete(false, "Error: ${exception.message}")
            } finally {
                LogUtil.log(TAG, "startDownload: END", LogUtil.LogType.DEBUG)
            }
        }
    }
