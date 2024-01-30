package manu.lucas.dev.ui.myHeros

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import manu.lucas.dev.data.local.RoomResponse
import manu.lucas.dev.ui.components.commons.RetrySection
import manu.lucas.dev.ui.components.myHeros.HeroBoughtRow
import manu.lucas.dev.ui.theme.RobotoCondensed
import manu.lucas.dev.util.convertPixelsToDp
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

        RetrySection(error = loadError, onRetry = {viewModel.getHerosBought()}, text = "Load your hero's !")
        }



@Composable
private fun initView(
    heroList: List<RoomResponse>,
    viewModel: MyHerosViewModel,
    navController: NavController,
    isLoading: Boolean,
    loadError:String
) {

    val listState = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth(),
        state = listState
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
    /**
     * Function which call the infinate scroll
     */
    listState.OnBottomReached {
        viewModel.getHerosBought()
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