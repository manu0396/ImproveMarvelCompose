package com.example.newmarvelcompose.data.remote.response.marvel

import com.example.newmarvelcompose.data.remote.response.marvel.ComicDataContainerResponse
import com.google.gson.annotations.SerializedName

data class ComicDataWrapperResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("copyright") val copyright: String,
    @SerializedName("attributionText") val attributionText: String,
    @SerializedName("data") val data: ComicDataContainerResponse
)
