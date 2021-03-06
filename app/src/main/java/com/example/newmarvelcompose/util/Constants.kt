package com.example.newmarvelcompose.util

import android.content.Context
import android.util.DisplayMetrics
import com.google.android.gms.wallet.WalletConstants

object Constants {
    const val BASE_URL ="https://gateway.marvel.com/"
    const val PAGE_SIZE =20
    const val DATABASE_NAME="heros"
    /**
     * This file contains several constants you must edit before proceeding.
     * Please take a look at PaymentsUtil.java to see where the constants are used and to potentially
     * remove ones not relevant to your integration.
     *
     *
     * Required changes:
     *
     *  1.  Update SUPPORTED_NETWORKS and SUPPORTED_METHODS if required (consult your processor if
     * unsure)
     *  1.  Update CURRENCY_CODE to the currency you use.
     *  1.  Update SHIPPING_SUPPORTED_COUNTRIES to list the countries where you currently ship. If this
     * is not applicable to your app, remove the relevant bits from PaymentsUtil.java.
     *  1.  If you're integrating with your `PAYMENT_GATEWAY`, update
     * PAYMENT_GATEWAY_TOKENIZATION_NAME and PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS per the
     * instructions they provided. You don't need to update DIRECT_TOKENIZATION_PUBLIC_KEY.
     *  1.  If you're using `DIRECT` integration, please edit protocol version and public key as
     * per the instructions.
     */
        private val GATEWAY_MERCHANT_ID: String = "9560-6061-8438"

        /**
         * Changing this to ENVIRONMENT_PRODUCTION will make the API return chargeable card information.
         * Please refer to the documentation to read about the required steps needed to enable
         * ENVIRONMENT_PRODUCTION.
         *
         * @value #PAYMENTS_ENVIRONMENT
         */
        const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST

        /**
         * The allowed networks to be requested from the API. If the user has cards from networks not
         * specified here in their account, these will not be offered for them to choose in the popup.
         *
         * @value #SUPPORTED_NETWORKS
         */
        val SUPPORTED_NETWORKS = listOf(
            "AMEX",
            "DISCOVER",
            "JCB",
            "MASTERCARD",
            "VISA")

        /**
         * The Google Pay API may return cards on file on Google.com (PAN_ONLY) and/or a device token on
         * an Android device authenticated with a 3-D Secure cryptogram (CRYPTOGRAM_3DS).
         *
         * @value #SUPPORTED_METHODS
         */
        val SUPPORTED_METHODS = listOf(
            "PAN_ONLY",
            "CRYPTOGRAM_3DS")

        /**
         * Required by the API, but not visible to the user.
         *
         * @value #COUNTRY_CODE Your local country
         */
        const val COUNTRY_CODE = "ES"

        /**
         * Required by the API, but not visible to the user.
         *
         * @value #CURRENCY_CODE Your local currency
         */
        const val CURRENCY_CODE = "EUR"

        /**
         * Supported countries for shipping (use ISO 3166-1 alpha-2 country codes). Relevant only when
         * requesting a shipping address.
         *
         * @value #SHIPPING_SUPPORTED_COUNTRIES
         */
        val SHIPPING_SUPPORTED_COUNTRIES = listOf("US", "GB", "ES")

        /**
         * The name of your payment processor/gateway. Please refer to their documentation for more
         * information.
         *
         * @value #PAYMENT_GATEWAY_TOKENIZATION_NAME
         */
        /**
        SE CONFIGURA INTEGRACION CON Mastercard Payment Gateway Services
         */
        const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "mpgs"

        /**
         * Custom parameters required by the processor/gateway.
         * In many cases, your processor / gateway will only require a gatewayMerchantId.
         * Please refer to your processor's documentation for more information. The number of parameters
         * required and their names vary depending on the processor.
         *
         * @value #PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS
         */
        val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
            "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
            "gatewayMerchantId" to GATEWAY_MERCHANT_ID
        )

        /**
         * Only used for `DIRECT` tokenization. Can be removed when using `PAYMENT_GATEWAY`
         * tokenization.
         *
         * @value #DIRECT_TOKENIZATION_PUBLIC_KEY
         */
        const val DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME"

        /**
         * Parameters required for `DIRECT` tokenization.
         * Only used for `DIRECT` tokenization. Can be removed when using `PAYMENT_GATEWAY`
         * tokenization.
         *
         * @value #DIRECT_TOKENIZATION_PARAMETERS
         */
        val DIRECT_TOKENIZATION_PARAMETERS = mapOf(
            "protocolVersion" to "ECv1",
            "publicKey" to DIRECT_TOKENIZATION_PUBLIC_KEY
        )

    }
fun convertPixelsToDp(px: Int, context: Context): Int {
    return px / (context.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}
