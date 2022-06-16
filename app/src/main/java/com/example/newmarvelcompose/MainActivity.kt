package com.example.newmarvelcompose

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.navArgument
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.example.newmarvelcompose.data.models.MarvelRoom
import com.example.newmarvelcompose.ui.myHeros.MyHerosViewModel
import com.example.newmarvelcompose.ui.nav.BottomNavigationBar
import com.example.newmarvelcompose.ui.nav.Screens
import com.example.newmarvelcompose.ui.myHeros.MyHerosScreen
import com.example.newmarvelcompose.ui.herodetail.HeroDetailScreen
import com.example.newmarvelcompose.ui.herodetail.HeroDetailViewModel
import com.example.newmarvelcompose.ui.herolist.HeroListScreen
import com.example.newmarvelcompose.ui.herolist.HeroListViewModel
import com.example.newmarvelcompose.ui.theme.JetpackComposeHeroTheme
import com.example.newmarvelcompose.util.PaymentsUtil
import com.example.newmarvelcompose.util.WrapperResponse
import com.example.newmarvelcompose.util.convertPixelsToDp
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var marvelBought: MarvelRoom? = null
    private lateinit var paymentsClient: PaymentsClient
    private lateinit var viewmodel: MainViewModel
    private lateinit var viewModelBoughtScreen: MyHerosViewModel
    private lateinit var viewModelDetail: HeroDetailViewModel
    private lateinit var viewModelList: HeroListViewModel
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentsClient = PaymentsUtil.createPaymentsClient(this)
        setContent {
            viewmodel = hiltViewModel()
            viewModelBoughtScreen = hiltViewModel()
            viewModelDetail = hiltViewModel()
            viewModelList = hiltViewModel()
            initView()
        }
    }
    @Composable
    private fun initView() {
        JetpackComposeHeroTheme {
            //contains screens and navigate use navcontroller and navhost
            val navController = rememberNavController()
            val height = Resources.getSystem().displayMetrics.heightPixels
            val heightDp = convertPixelsToDp(px = height*(3/2), context = applicationContext)
            Scaffold(
                bottomBar = { BottomNavigationBar(heightBottomBar = heightDp/10,navController = navController) }
                    ) {
                NavigationGraph(navController)
            }
        }
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = Screens.List.route) {

            composable(Screens.List.route) {
                HeroListScreen(
                    navController = navController,
                    viewModel = viewModelList,
                    context = applicationContext
                )
            }
            composable(
                Screens.Detail.route,
                // use as URL
                arguments = listOf(
                    navArgument("dominantColor") {
                        type = NavType.IntType
                    },
                    navArgument("number") {
                        type = NavType.IntType
                    }
                ),
            ) {
                val dominantColor = remember {
                    val color = it.arguments?.getInt("dominantColor")
                    color?.let { Color(it) } ?: Color.White
                }
                val heroNumber = remember {
                    it.arguments?.getInt("number")
                }

                if (heroNumber != null) {
                    HeroDetailScreen(
                        activity = this@MainActivity,
                        context = applicationContext,
                        dominantColor = dominantColor,
                        heroNumber = heroNumber!!.toLong(),
                        navController = navController,
                        viewModel = viewModelDetail
                    )
                }
            }

            composable(Screens.Bought.route) {
                MyHerosScreen(
                    context = applicationContext,
                    dominantColor = MaterialTheme.colors.background,
                    navController = navController,
                    viewModel = viewModelBoughtScreen
                )
            }
        }
    }

    fun payWithGooglePay(price: String, marvel: MarvelRoom) {
        //Set the marvel bought
        marvelBought = marvel
        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price)
        Log.d("payment", "paymentDataRequestJson: $paymentDataRequestJson")
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            Log.d("payment", "resquest != null: ${request.toJson()}")
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }
                    Activity.RESULT_CANCELED -> {
                        // Nothing to do here normally - the user simply cancelled without selecting a
                        // payment method.
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
                // Re-enables the Google Pay payment button.
               // googlePayButton.isClickable = true
            }
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see [Payment
     * Data](https://developers.google.com/pay/api/android/reference/object.PaymentData)
     */
    private fun handlePaymentSuccess(paymentData: PaymentData) {
        Log.d("payment", "paymentDataResponse ${paymentData.toJson()}")
        val paymentInformation = paymentData.toJson() ?: return
        Log.d("payment", "paymentInformation $paymentInformation")
        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            Log.d("payment", "paymentMethodData $paymentMethodData")
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("type") == "PAYMENT_GATEWAY" && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token") == "examplePaymentMethodToken") {

                AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("Gateway name set to \"example\" - please modify " +
                            "Constants.java and replace it with your own gateway.")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }

            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            if(marvelBought!!.bought == 1){
                Toast.makeText(this, "You already have that hero. Try to buy another.", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Succesful payment for ${marvelBought!!.name} by $billingName", Toast.LENGTH_LONG).show()
                marvelBought?.bought = 1
                val resp = viewmodel.insertHeroBought(marvelBought!!)
                when(resp){
                    is WrapperResponse.Sucess ->{
                        if(!resp.equals(-1)){
                            Log.d("Insert", "Inserta el hero localmente")
                        }
                    }
                    is WrapperResponse.Error ->{
                        Log.d("Insert", "Falla al insertar el hero")
                    }
                }
            }
            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }

    fun possiblyShowGooglePayButton(): Boolean {
        var isReady = true
        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return false
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return false
        Log.d("payment", "isReadyToPayJson $isReadyToPayJson")
        Log.d("payment", "request $request")
        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)
            } catch (exception: ApiException) {
                isReady = false
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
        return isReady
    }
}


