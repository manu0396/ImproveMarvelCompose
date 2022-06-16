package com.example.newmarvelcompose.data.remote.response.marvel

import com.google.gson.annotations.SerializedName


data class CharacterDataContainerResponse(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<ComicCharacterResponse>)

