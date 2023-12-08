package com.example.newmarvelcompose.ui.components.list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.ui.herolist.HeroListViewModel
import com.example.newmarvelcompose.ui.theme.RobotoCondensed
import com.skydoves.landscapist.coil.CoilImage


@SuppressLint("LogNotTimber")
@Composable
fun HeroEntry(
    model: MarvelListModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HeroListViewModel,
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(dominantColor, defaultDominantColor)
                )
            )
            .clickable {
                navController.navigate(
                    "detail_screen/${dominantColor.toArgb()}/${model.id}"
                )
            }

    ) {
        Column {
            CoilImage(
                imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(model.thumbnail)
                    .target {
                        viewModel.calcDominantColor(it) { color ->
                            dominantColor = color
                        }
                    }.build(),
                imageLoader = {
                    ImageLoader.Builder(LocalContext.current)
                        .memoryCache(
                            MemoryCache.Builder(LocalContext.current).maxSizePercent(0.25).build()
                        )
                        .crossfade(true)
                        .build()
                },
                contentDescription = model.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                loading = {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val indicator = createRef()
                        CircularProgressIndicator(
                            //Set constrains dynamically
                            modifier = Modifier.constrainAs(indicator) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )
                    }
                },
                // shows an error text message when request failed.
                failure = {
                    Text(text = "image request failed.")
                }
            )

            Text(
                text = model.name.split("(")[0],
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(0.dp, 0.dp, 0.dp, 2.dp),

                )
        }
    }
}