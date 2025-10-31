package com.kimothorick.plashr.utils

import android.content.Context
import android.content.Intent
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import org.ocpsoft.prettytime.PrettyTime
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Converts an ISO-8601 date string into a human-readable format.
 *
 * This extension function parses a string representing a date and time in ISO-8601 format
 * (e.g., "2023-10-27T10:15:30Z") and formats it into a more readable date string
 * like "October 27, 2023". The conversion uses the system's default time zone.
 *
 * @receiver String The ISO-8601 formatted date string to convert.
 * @return A formatted date string in the "MMMM dd, yyyy" pattern.
 * @throws java.time.format.DateTimeParseException if the string cannot be parsed.
 */
fun String.toFormattedDate(): String {
    val instant = Instant.parse(this)
    val formatter =
        DateTimeFormatter.ofPattern("MMMM dd, yyyy").withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

/**
 * Formats an ISO 8601 date string into a human-readable "time ago" format.
 *
 * This extension function takes a nullable String, which is expected to be an ISO 8601
 * timestamp (e.g., "2023-10-27T10:00:00Z"). It then uses the `PrettyTime` library
 * to convert this timestamp into a relative time phrase like "a moment ago",
 * "5 minutes ago", or "2 days ago".
 *
 * The final output is prefixed with "Last updated ".
 *
 * - If the receiver string is `null`, it returns "Last updated: Unknown".
 * - If the string is not a valid ISO 8601 format, it returns "Last updated: Invalid date".
 *
 * @receiver A nullable String representing a date in ISO_OFFSET_DATE_TIME format.
 * @return A formatted string indicating the time elapsed since the given date.
 */
fun String?.getFormattedUpdatedAtPretty(): String { // Renamed slightly to avoid clash if you keep old one
    if (this == null) { // `this` refers to the String instance the function is called on
        return "Last updated: Unknown"
    }

    return try {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val pastDate = OffsetDateTime.parse(this, formatter) // Use `this`
        val pastDateAsJavaUtilDate = Date.from(pastDate.toInstant())
        val prettyTime = PrettyTime()
        val timeAgo = prettyTime.format(pastDateAsJavaUtilDate)
        "Last updated $timeAgo"
    } catch (_: Exception) {
        "Last updated: Invalid date"
    }
}

/**
 * Creates and starts an Android share intent to share a text link.
 *
 * This function constructs an `ACTION_SEND` intent with the provided link as plain text.
 * It then wraps this intent in a chooser, allowing the user to select which app they want
 * to use for sharing, and immediately starts the activity.
 *
 * @param linkToShare The URL or text string to be shared.
 * @param context The Context from which the share intent should be launched.
 */
fun shareLinkIntent(
    linkToShare: String,
    context: Context,
) {
    val shareActionIntent: Intent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, linkToShare)
            type = "text/plain"
        }

    val chooserIntent = Intent.createChooser(shareActionIntent, null)
    context.startActivity(chooserIntent)
}

/**
 * Formats a location string from a city and a country.
 *
 * This function takes nullable city and country strings, trims any leading/trailing whitespace,
 * and combines them into a single, user-friendly location string.
 * - If both city and country are provided, it returns them in the format "City, Country".
 * - If only one is provided (and not blank), it returns that one.
 * - If both are null, empty, or consist only of whitespace, it returns an empty string.
 *
 * @param city The name of the city. Can be null or blank.
 * @param country The name of the country. Can be null or blank.
 * @return A formatted location string, or an empty string if no valid location data is provided.
 */
fun formatLocation(
    city: String?,
    country: String?,
): String {
    val formattedCity = city?.trim()
    val formattedCountry = country?.trim()

    return when {
        formattedCity.isNullOrBlank() && formattedCountry.isNullOrBlank() -> ""
        formattedCity.isNullOrBlank() -> formattedCountry!!
        formattedCountry.isNullOrBlank() -> formattedCity
        else -> "$formattedCity, $formattedCountry"
    }
}

/**
 * Represents the different width classes for a window, based on Material Design guidelines.
 *
 * This enum helps in creating responsive UIs that adapt to different screen sizes.
 * It's derived from the `WindowSizeClass` provided by `androidx.window.core.layout`.
 *
 * The width classes are defined as follows:
 * - **COMPACT**: Represents a typical phone in portrait mode.
 * - **MEDIUM**: Represents a typical phone in landscape mode or a tablet in portrait mode.
 * - **EXPANDED**: Represents a typical tablet in landscape mode or a desktop screen.
 *
 * The actual dp breakpoints are defined by `androidx.window.core.layout.WindowSizeClass`.
 *
 * @see WindowSizeClass
 * @see windowWidthSize
 */
enum class WindowWidthClass {
    COMPACT,
    MEDIUM,
    EXPANDED,
}

/**
 * Determines the [WindowWidthClass] based on the current [WindowSizeClass].
 *
 * This function categorizes the window width into one of three classes:
 * - [WindowWidthClass.EXPANDED] if the width is greater than or equal to `WIDTH_DP_EXPANDED_LOWER_BOUND`.
 * - [WindowWidthClass.MEDIUM] if the width is greater than or equal to `WIDTH_DP_MEDIUM_LOWER_BOUND` but less than `WIDTH_DP_EXPANDED_LOWER_BOUND`.
 * - [WindowWidthClass.COMPACT] if the width is less than `WIDTH_DP_MEDIUM_LOWER_BOUND`.
 *
 * This is useful for adapting UI layouts to different screen widths.
 *
 * @receiver The [WindowSizeClass] instance from which to determine the width class.
 * @return The corresponding [WindowWidthClass].
 * @see WindowSizeClass
 * @see WindowWidthClass
 * @see WindowSizeClass.isWidthAtLeastBreakpoint
 * @see WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
 * @see WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
 */
fun WindowSizeClass.windowWidthSize(): WindowWidthClass =
    when {
        isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
            WindowWidthClass.EXPANDED
        }

        isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            WindowWidthClass.MEDIUM
        }

        else -> {
            WindowWidthClass.COMPACT
        }
    }
