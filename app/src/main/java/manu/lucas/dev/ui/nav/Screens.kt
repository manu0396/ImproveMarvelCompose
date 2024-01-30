package manu.lucas.dev.ui.nav

sealed class Screens(val route: String) {
    object List : Screens("list_screen")
    object Bought : Screens("bought_screen")
    object Detail : Screens("detail_screen/{dominantColor}/{number}")
}
