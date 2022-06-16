package com.example.newmarvelcompose.data.remote.response.marvel

import com.example.newmarvelcompose.data.remote.response.marvel.CharacterDataContainerResponse
import com.google.gson.annotations.SerializedName

data class CharacterResultResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("copyright") val copyright: String,
    @SerializedName("attributionText") val attributionText: String,
    @SerializedName("data") val dataResponse: CharacterDataContainerResponse
)

