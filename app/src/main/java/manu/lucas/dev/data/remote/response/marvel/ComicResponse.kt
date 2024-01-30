package manu.lucas.dev.data.remote.response.marvel

import com.google.gson.annotations.SerializedName


data class ComicResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = "",
    @SerializedName("thumbnail") val thumbnailResponse: ThumbnailResponse
)