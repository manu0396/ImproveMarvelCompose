package com.example.newmarvelcompose.ui.herodetail

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.newmarvelcompose.ui.components.detail.HeroDetailStateWrapper
import com.example.newmarvelcompose.ui.components.detail.HeroDetailTopSection
import com.skydoves.landscapist.coil.CoilImage

const val price = 14.7

@Composable
fun HeroDetailScreen(
    activity: Activity,
    context: Context,
    dominantColor: Color,
    heroNumber: Long,
    navController: NavController,
    topPadding: Dp = 20.dp,
    heroImageSize: Dp = 200.dp,
    viewModel: HeroDetailViewModel,
) {
    LaunchedEffect(key1 = heroNumber) {
        viewModel.getHeroInfo(heroNumber)
    }
    val heroInfo by viewModel.hero.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val loadError by viewModel.loadError.collectAsState()

    when {
        isLoading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight(0.4f)
                        .align(Alignment.Center)
                )
            }
        }

        loadError != "" -> {
            Text(
                text = "Se ha producido un error",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }

        else -> {
            Scaffold {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black,
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(bottom = 16.dp)
                ) {

                    HeroDetailTopSection(
                        activity = activity,
                        navController = navController,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f)
                            .align(Alignment.TopCenter),
                        context = context,
                        viewModel = viewModel,
                        heroInfo = heroInfo[0],
                        heroNumber = heroNumber.toInt()
                    )
                    Spacer(
                        modifier = Modifier
                            .size(1.dp, 80.dp)
                            .background(Color.LightGray)
                    )
                    HeroDetailStateWrapper(
                        heroInfo = heroInfo[0],
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = topPadding + heroImageSize / 2f,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                            .shadow(10.dp, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colors.surface)
                            .padding(16.dp)
                            .align(Alignment.BottomCenter),
                        loadingModifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                            .padding(
                                top = topPadding + heroImageSize / 2f,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        heroInfo[0].thumbnail.let {
                            CoilImage(
                                contentScale = ContentScale.Crop, // crop the image if it's not a square
                                imageRequest = ImageRequest.Builder(LocalContext.current)
                                    .data(it)
                                    .crossfade(true)
                                    .build(),
                                imageLoader = {
                                    ImageLoader.Builder(LocalContext.current)
                                        .memoryCache(
                                            MemoryCache.Builder(LocalContext.current)
                                                .maxSizePercent(0.25).build()
                                        )
                                        .crossfade(true)
                                        .build()
                                },
                                contentDescription = heroInfo[0].name,
                                modifier = Modifier
                                    .size(heroImageSize)
                                    .offset(y = topPadding)
                                    .clip(CircleShape)    // clip to the circle shape
                                    .border(2.dp, Color.LightGray, CircleShape) // add a border
                            )
                        }
                    }
                }
            }
        }
    }
}




