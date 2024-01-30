package manu.lucas.dev.data.local

data class RoomResponse(
    val uid: String,
    val name: String,
    val image: String,
    val numberId: Int,
    val description: String,
    var bought: Int
)
