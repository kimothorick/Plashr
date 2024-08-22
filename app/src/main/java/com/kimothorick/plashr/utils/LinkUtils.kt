package com.kimothorick.plashr.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Opens a given URL in an external browser.
 *
 * This function creates an Intent with the ACTION_VIEW action and theprovided URL, then starts the activity to open the link.
 *
 * @param url The URL to open.
 * @param context The application context.
 */
fun OpenLink(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}