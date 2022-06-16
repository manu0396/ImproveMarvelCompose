package com.example.newmarvelcompose.data.remote.mapper

interface ResponseMapper<E,M> {
    fun fromResponse(response:E):M
}