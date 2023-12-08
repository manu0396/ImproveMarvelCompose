package com.example.newmarvelcompose.ui.components.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.ui.components.detail.HeroDetailSection

@Composable
fun HeroDetailStateWrapper(
    heroInfo: MarvelListModel,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    Spacer(
        modifier = Modifier
            .size(1.dp, 20.dp)
            .background(Color.LightGray)
    )

    HeroDetailSection(heroInfo = heroInfo)

}
