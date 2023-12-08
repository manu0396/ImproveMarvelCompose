package com.example.newmarvelcompose.ui.herolist

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newmarvelcompose.R
import com.example.newmarvelcompose.ui.components.commons.SearchBar
import com.example.newmarvelcompose.ui.components.list.HeroList
import com.example.newmarvelcompose.util.convertPixelsToDp

@Composable
fun HeroListScreen(
    navController: NavController,
    viewModel: HeroListViewModel,
    context: Context,
) {
    val height = Resources.getSystem().displayMetrics.heightPixels

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    )
    {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "Hero's",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(convertPixelsToDp(height / 8, context = context).dp)
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                viewModel = viewModel
            ) {
                //Here is where we define the function pass as argument: onSearch
                //viewModel.searchheroList(it)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HeroList(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}








