package com.example.newmarvelcompose.data.remote.response.marvel

import com.google.gson.annotations.SerializedName

data class ComicCharacterResponse(@SerializedName("id") val id: Long,
                                  @SerializedName("name") val name: String,
                                  @SerializedName("description") val description: String,
                                  @SerializedName("modified") val modified: String,
                                  @SerializedName("resourceUri") val resourceUri: String,
                                  @SerializedName("thumbnail") val thumbnailResponse: ThumbnailResponse,
                                  val price: Double = 14.7)
