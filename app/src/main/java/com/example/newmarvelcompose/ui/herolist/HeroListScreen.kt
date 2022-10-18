package com.example.newmarvelcompose.ui.herolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.newmarvelcompose.data.models.MarvelListModel
import com.example.newmarvelcompose.ui.theme.RobotoCondensed
import com.example.newmarvelcompose.util.convertPixelsToDp
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun HeroListScreen(
    navController: NavController,
    viewModel: HeroListViewModel,
    context: Context
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
            ){
                //Here is where we define the function pass as argument: onSearch
              //viewModel.searchheroList(it)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HeroList(navController = navController,
                        viewModel = viewModel)


        }
    }

}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = " ",
    viewModel: HeroListViewModel,
    onSearch: (String) -> Unit
) {

    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(value = text,
            onValueChange = {
                text = it
                //onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)

            )
        }
        if(!isHintDisplayed && text != ""){
            LaunchedEffect(key1 = text){
                viewModel.searchheroList(text)
            }
        }
    }
}

@Composable
fun HeroList(
    navController: NavController,
    viewModel: HeroListViewModel
) {

    val heroList by  viewModel.heroList.collectAsState()
    val endReached by viewModel.endReached.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSearching by remember { viewModel.isSearching }

    val listState = rememberLazyListState()

    LazyColumn(state = listState, contentPadding = PaddingValues(16.dp)) {

        val itemCount = if (heroList.size % 2 == 0) {
            heroList.size / 2
        } else {
            heroList.size / 2 + 1
        }

        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                viewModel.loadHeroPaginated()
            }
            HeroRow(rowIndex = it, models = heroList, navController = navController, viewModel = viewModel)
        }
    }
    /**
     * Function which call the infinate scroll
     */
    listState.OnBottomReached {
        viewModel.loadHeroPaginated()
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadHeroPaginated()
            }
        }
    }

}


@SuppressLint("LogNotTimber")
@Composable
fun HeroEntry(
    model: MarvelListModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HeroListViewModel
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Center,
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
                        .memoryCache(MemoryCache.Builder(LocalContext.current).maxSizePercent(0.25).build())
                        .crossfade(true)
                        .build()
                },
                contentDescription = model.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally),
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

@Composable
fun HeroRow(
    rowIndex: Int,
    models: List<MarvelListModel>,
    navController: NavController,
    viewModel: HeroListViewModel
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

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit,
) {
    Column() {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

/**
 * Component which allow infiniteScroll
 */
@Composable
fun LazyListState.OnBottomReached(
    loadMore : () -> Unit
){
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore){
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}


