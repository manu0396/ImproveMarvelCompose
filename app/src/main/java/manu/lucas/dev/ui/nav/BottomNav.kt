package manu.lucas.dev.ui.nav

import com.example.newmarvelcompose.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object BoughtScreen : BottomNavItem("My Hero's", R.drawable.ic_all_inbox,"bought_screen")
    object ListScreen: BottomNavItem("List",R.drawable.ic_list,"list_screen")
}