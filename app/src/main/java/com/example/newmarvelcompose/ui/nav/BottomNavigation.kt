package com.example.newmarvelcompose.ui.nav

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavigationBar(heightBottomBar: Int, navController: NavController, bottomNavState: Boolean) {

    val items = listOf(
            BottomNavItem.ListScreen,
            BottomNavItem.BoughtScreen
        )
    var visibility = if(bottomNavState){
        1f
    }else{
        0f
    }
    BottomNavigation(
        Modifier
            .height(heightBottomBar.dp)
            .alpha(visibility),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color.White)
    {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                selectedContentColor = androidx.compose.ui.graphics.Color.Black,
                unselectedContentColor = androidx.compose.ui.graphics.Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    //Added to manage navigate to List screen, instead of detail screen when user pop List tab from BoughtScreen.
                    if(item.title == "List"){
                        navController.navigate("list_screen")
                    }else{
                        navController.navigate(item.screen_route) {

                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }