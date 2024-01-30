package manu.lucas.dev.ui.components.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import manu.lucas.dev.data.models.MarvelListModel
import java.util.Locale

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
