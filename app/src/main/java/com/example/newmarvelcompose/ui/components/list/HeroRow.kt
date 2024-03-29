package com.example.newmarvelcompose.ui.components.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.ui.herolist.HeroListViewModel


@Composable
fun HeroRow(
    rowIndex: Int,
    models: List<MarvelListModel>,
    navController: NavController,
    viewModel: HeroListViewModel,
) {
    Column {
        Row {
            HeroEntry(
                model = models[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.width(16.dp))

            if (models.size >= rowIndex * 2 + 2) {
                HeroEntry(
                    model = models[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    viewModel = viewModel
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

}