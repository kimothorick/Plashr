package com.kimothorick.plashr.data.models.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class UserStats(
    @SerializedName("downloads") val downloads: Downloads?,
    @SerializedName("username") val username: String?,
    @SerializedName("views") val views: Views?,
) : Parcelable {
    @Parcelize
    @Serializable
    data class Downloads(
        @SerializedName("historical") val historical: HistoricalStats?, // Use the common class
        @SerializedName("total") val total: Int?,
    ) : Parcelable

    @Parcelize
    @Serializable
    data class Views(
        @SerializedName("historical") val historical: HistoricalStats?, // Use the common class
        @SerializedName("total") val total: Int?,
    ) : Parcelable
}

@Parcelize
@Serializable
data class HistoricalStatsValue(
    // Renamed from Value to avoid naming conflict
    @SerializedName("date") val date: String?,
    @SerializedName("value") val value: Int?,
) : Parcelable

@Parcelize
@Serializable
data class HistoricalStats(
    @SerializedName("average") val average: Int?,
    @SerializedName("change") val change: Int?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("resolution") val resolution: String?,
    @SerializedName("values") val values: List<HistoricalStatsValue?>?,
) : Parcelable
