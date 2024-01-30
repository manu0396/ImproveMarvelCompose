package manu.lucas.dev.ui.components.detail

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import manu.lucas.dev.MainActivity
import com.example.newmarvelcompose.R
import manu.lucas.dev.data.models.MarvelListModel
import manu.lucas.dev.data.models.MarvelRoom
import manu.lucas.dev.ui.herodetail.HeroDetailViewModel
import manu.lucas.dev.ui.herodetail.price
import manu.lucas.dev.util.convertPixelsToDp


@Composable
fun HeroDetailTopSection(
    activity: Activity,
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
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
