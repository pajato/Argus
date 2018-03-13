package com.pajato.argus.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.pajato.argus.event.PurchaseUpdateEvent
import com.pajato.argus.event.RxBus
import com.pajato.argus.event.UserCancelledBillingEvent

object BillingManager : PurchasesUpdatedListener, BillingClientStateListener, SkuDetailsResponseListener, PurchaseHistoryResponseListener {
    val MULTIPLE_DEVICE_SUPPORT_KEY = "firebase_cloud_storage"

    lateinit var client: BillingClient
    private var isConnected = false
    private var executeOnSuccess: Runnable? = null

    var mPremiumUpgradePrice = ""
    var mGasPrice = ""
    var mMultipleDevicePrice = ""

    /** Initialize the Google Play billing client and begin the connection. */
    fun init(context: Context) {
        client = BillingClient.newBuilder(context).setListener(this).build()
        client.startConnection(this)
    }

    /** End the Google Play billing client's connection. */
    fun destroy() {
        client.endConnection()
    }

    /** A BillingClientStateListener method that indicates the connection to the Google Play client has ended. */
    override fun onBillingServiceDisconnected() {
        // Try to restart the connection on the next request to Google Play by calling the startConnection() method
        isConnected = false
    }

    /**
     * A BillingClientStateListener method that indicates the connection to the Google Play client
     * has begun. If the connection is successful and there is an operation waiting to execute, then
     * execute it.
     */
    override fun onBillingSetupFinished(responseCode: Int) {
        isConnected = responseCode == BillingClient.BillingResponse.OK
        if (isConnected && executeOnSuccess != null) {
            executeOnSuccess?.run()
            executeOnSuccess = null
        }
    }

    /** A PurchaseHistoryResponseListener method that delivers a purchase history. */
    override fun onPurchaseHistoryResponse(responseCode: Int, purchasesList: MutableList<Purchase>?) {
        if (responseCode == BillingClient.BillingResponse.OK && purchasesList != null) {
            for (purchase in purchasesList) {
                // Process purchase result.
                handlePurchase(purchase)
            }
        }
    }

    /** A PurchasesUpdatedListener method that indicates a purchase has gone through. */
    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow
            RxBus.send(UserCancelledBillingEvent())
        } else {
            // Handle any other error codes.
        }
    }

    /** A SkuDetailsResponseListener method that delivers information on what items are purchasable. */
    override fun onSkuDetailsResponse(responseCode: Int, skuDetailsList: MutableList<SkuDetails>?) {
        // Process the result of the Query
        if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
            for (skuDetails in skuDetailsList) {
                val sku = skuDetails.sku
                val price = skuDetails.price
                when (sku) {
                    "premium_upgrade" -> mPremiumUpgradePrice = price
                    "gas" -> mGasPrice = price
                    MULTIPLE_DEVICE_SUPPORT_KEY -> mMultipleDevicePrice = price
                }
            }
        }
    }

    /** A way to ensure our client is always connected when we want to begin operations. */
    private fun executeServiceRequest(runnable: Runnable) {
        if (client.isReady && isConnected) {
            runnable.run()
        } else {
            client.startConnection(this)
        }
    }

    /** Unlock a service based on the purchase. */
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.sku == MULTIPLE_DEVICE_SUPPORT_KEY) {
            Log.v(this.javaClass.canonicalName, "Valid Purchase Identified: " + purchase.sku)
            RxBus.send(PurchaseUpdateEvent(purchase))
        }
    }

    /** Make a subscription purchase. */
    fun subscribe(sku: String, activity: Activity) {
        executeServiceRequest(Runnable {

            val params = BillingFlowParams.newBuilder()
                    .setSku(sku)
                    .setType(BillingClient.SkuType.SUBS)
                    .build()
            if (client.isReady) {
                val responseCode = client.launchBillingFlow(activity, params)
            } else {

            }
        })
    }

    fun purchase(sku: String, activity: Activity) {
        executeServiceRequest(Runnable {

            val params = BillingFlowParams.newBuilder()
                    .setSku(sku)
                    .setType(BillingClient.SkuType.INAPP)
                    .build()
            val responseCode = client.launchBillingFlow(activity, params)

        })
    }

    /** Determine what subscriptions have been purchased. */
    fun querySubscriptionsPurchaseable() {
        executeServiceRequest(Runnable {
            val skuList = ArrayList<String>()
            skuList.add(MULTIPLE_DEVICE_SUPPORT_KEY)

            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
            client.querySkuDetailsAsync(params.build(), this)
        })
    }

    fun queryItemsPurchaseable() {
        executeServiceRequest(Runnable {

            val skuList = ArrayList<String>()
            skuList.add("premium_upgrade")
            skuList.add("gas")
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
            client.querySkuDetailsAsync(params.build(), this)

        })
    }

    /** Determine if any subscriptions have been purchased. */
    fun querySubscriptionsPurchased() {
        executeServiceRequest(Runnable {
            val result = client.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, this)
        })
    }

    fun queryItemsPurchased() {
        executeServiceRequest(Runnable {

            val result = client.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)

        })
    }
}
