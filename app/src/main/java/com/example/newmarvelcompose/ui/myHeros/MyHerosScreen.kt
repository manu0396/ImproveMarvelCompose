package com.example.newmarvelcompose.ui.myHeros

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.newmarvelcompose.R
import com.example.newmarvelcompose.data.local.RoomResponse
import com.example.newmarvelcompose.ui.herolist.RetrySection
import com.example.newmarvelcompose.ui.theme.RobotoCondensed
import com.example.newmarvelcompose.util.convertPixelsToDp
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun MyHerosScreen (
    context: Context,
    dominantColor: Color,
    navController: NavController,
    viewModel: MyHerosViewModel
) {

    viewModel.getHerosBought()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                setImagesTitle(
                    resource = R.drawable.ic_yours,
                    modifier = Modifier.apply {
                        align(CenterVertically)
                        padding(20.dp)
                    }
                )
                setImagesTitle(
                    resource = R.drawable.ic_heros,
                    modifier = Modifier.align(CenterVertically)
                    )
            }
            Spacer(modifier = Modifier.height(20.dp))

                HeroListRoom(
                    navController = navController,
                    context = context,
                    viewModel = viewModel
                )
            }
        }
    }

@Composable
fun setImagesTitle(resource: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = resource),
        contentDescription = "Cabecera",
        modifier = modifier
            .padding(5.dp)
        )
}
@Composable
fun HeroListRoom(
    navController: NavController,
    viewModel: MyHerosViewModel,
    context: Context,
){

    val heroList by viewModel.heroBoughtList.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    Log.d("http", "heroList: ${heroList.size}")

    val height = Resources.getSystem().displayMetrics.heightPixels
    val heightDp = convertPixelsToDp(px = height/2, context = context)
    Spacer(modifier = Modifier.height(20.dp))
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.apply {
            background(Color.LightGray)
            fillMaxWidth()
            height(heightDp.dp)
        }
    ) {
        if(heroList.isEmpty()){
            initEmptyList(context = context, viewModel = viewModel, loadError = loadError)
        }else{
            initView(heroList, viewModel, navController, isLoading, loadError)
        }
    }
}

@Composable
private fun initEmptyList(context: Context, viewModel: MyHerosViewModel, loadError: String) {

        Text(
            text = context.getString(R.string.no_hero_bought),
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        )

        RetryLoad(error = loadError, onRetry = {viewModel.getHerosBought()}, text = "Load your hero's !")
        }



@Composable
private fun initView(
    heroList: List<RoomResponse>,
    viewModel: MyHerosViewModel,
    navController: NavController,
    isLoading: Boolean,
    loadError:String
) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val itemCount = if (heroList.size % 2 == 0) {
            heroList.size / 2
        } else {
            heroList.size / 2 + 1
        }

        items(itemCount) {
            HeroBoughtRow(rowIndex = it, entries = heroList, navController = navController, viewModel = viewModel)
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.getHerosBought()
            }
        }
    }
}


@Composable
fun HeroBoughtRow(
    rowIndex: Int,
    entries: List<RoomResponse>,
    navController: NavController,
    viewModel: MyHerosViewModel
) {
    Column {
        Row {

            HeroRoomEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.width(16.dp))

            if (entries.size >= rowIndex * 2 + 2) {

                HeroRoomEntry(
                    entry = entries[(rowIndex * 2) + 1],
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



@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("LogNotTimber")
@Composable
fun HeroRoomEntry(
    entry: RoomResponse,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyHerosViewModel
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
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        "detail_screen/${dominantColor.toArgb()}/${entry.numberId}"
                    )
                },
                onLongClick = {
                    viewModel.onOpenDialogClicked(entry.name)
                },
            )

    ) {
        val showDialogState: Boolean by viewModel.showDialog.collectAsState()
        if(showDialogState){
            Log.d("pokemon", "pokemon to remove ${viewModel.heroToDelete.value}")

            SimpleAlertDialog(
                hero = viewModel.heroToDelete.value,
                show = showDialogState,
                onConfirm = { viewModel.onDialogConfirm(viewModel.heroToDelete.value) },
                onDismiss = viewModel::onDialogDismiss)
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CoilImage(
                imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(entry.image)
                    .target {
                        viewModel.calcDominantColor(it) { color ->
                            dominantColor = color
                        }
                    }.build(),
                imageLoader = {
                    ImageLoader.Builder(LocalContext.current)
                        .memoryCache(MemoryCache.Builder(LocalContext.current).maxSizePercent(0.25).build())
                        .crossfade(true)
                        .build()
                },
                contentDescription = entry.name,
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

            Log.d("herolist", entry.image)
            Text(
                text = entry.name.split("(")[0],
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.apply {
                    fillMaxWidth()
                    padding(start = 0.dp,top = 0.dp, bottom = 10.dp, end = 0.dp)
                }
            )

        }

    }
}

//At the begining is invisible and show when the user try to remove some pokemon.
@Composable
fun SimpleAlertDialog(
    hero: String,
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if(show){
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Remove") },
            text = { Text(text = "Would you like to remove ${hero} from your hero's?") }
        )
    }
}


@Composable
fun RetryLoad(
    text: String,
    error: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.apply {
            fillMaxWidth()
            CenterHorizontally
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = text)
        }
    }
}