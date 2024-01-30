package manu.lucas.dev.ui.components.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import manu.lucas.dev.ui.components.commons.RetrySection
import manu.lucas.dev.ui.herolist.HeroListViewModel


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
        contentAlignment = Alignment.Center,
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
/**
 * Component which allow infiniteScroll
 */
@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit,
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}