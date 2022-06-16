package com.example.newmarvelcompose.ui.herodetail

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.example.newmarvelcompose.R
import com.skydoves.landscapist.coil.CoilImage
import java.util.*
import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import coil.ImageLoader
import coil.memory.MemoryCache
import com.example.newmarvelcompose.MainActivity
import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.data.models.MarvelRoom
import com.example.newmarvelcompose.util.convertPixelsToDp
import com.example.newmarvelcompose.util.*

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
    viewModel: HeroDetailViewModel
) {
    LaunchedEffect(key1 = heroNumber) {
        viewModel.getHeroInfo(heroNumber)
    }
    val heroInfo by viewModel.hero.collectAsState()

   val isLoading by viewModel.isLoading.collectAsState()

   val loadError by viewModel.loadError.collectAsState()

    val height = Resources.getSystem().displayMetrics.heightPixels
    val width = Resources.getSystem().displayMetrics.widthPixels

    if(isLoading){
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
    }else if(loadError!=""){
        Text(
            text = "Se ha producido un error",
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }else{
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
                            .data("$it")
                            .crossfade(true)
                            .build(),
                        imageLoader = {
                            ImageLoader.Builder(LocalContext.current)
                                .memoryCache(MemoryCache.Builder(LocalContext.current).maxSizePercent(0.25).build())
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

@Composable
fun HeroDetailTopSection(
    activity: Activity,
    navController: NavController,
    modifier: Modifier = Modifier,
    context:Context,
    heroNumber: Int,
    viewModel: HeroDetailViewModel,
    heroInfo: MarvelListModel?
)
{
    val isReadyToPay = (activity as MainActivity).possiblyShowGooglePayButton()
    var imageVisibility = 0f
    if(isReadyToPay){
        imageVisibility = 1f
    }
    val width = Resources.getSystem().displayMetrics.widthPixels
    val widthDp = convertPixelsToDp(px = width/2, context = context)
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
            Image(
                painter = painterResource(id = R.drawable.buy_with_googlepay_button_content),
                contentDescription = null,
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .alpha(imageVisibility)
                    .offset((widthDp / 2).dp, 16.dp)
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.6f)
                    .background(Color.Transparent)
                    .clickable {
                        if (!isReadyToPay) {
                            Toast
                                .makeText(
                                    context,
                                    activity.getString(R.string.googlepay_status_unavailable),
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        } else {
                            (activity as MainActivity).payWithGooglePay(
                                "$price", MarvelRoom(
                                    id = heroInfo?.id,
                                    numberId = heroInfo!!.id,
                                    name = heroInfo?.name!!,
                                    image = "${heroInfo?.thumbnail}",
                                    bought = 0,
                                    description = heroInfo.description
                                )
                            )
                        }
                    }
                )
        Spacer(
            modifier = Modifier
                .size(1.dp, 80.dp)
                .background(Color.LightGray)
                )
            }
        }


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


@Composable
fun HeroDetailSection(
    heroInfo: MarvelListModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${heroInfo.id}: ${heroInfo.name.capitalize(Locale.ROOT).split("(")[0]}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, 20.dp)
        )
        Text(
            text = "${heroInfo.description.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Thin,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, 20.dp)
        )
    }
}
