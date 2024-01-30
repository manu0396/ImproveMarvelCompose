package manu.lucas.dev.data.remote.mapper

interface ResponseMapper<E,M> {
    fun fromResponse(response:E):M
}