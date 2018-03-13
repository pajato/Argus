package com.pajato.argus.event

import com.android.billingclient.api.Purchase
import io.reactivex.functions.Consumer

class PurchaseUpdateEvent(purchase: Purchase) : Event {
    private var data = purchase

    override fun getData(): Purchase {
        return data
    }

    interface PurchaseUpdateEventListener: Consumer<PurchaseUpdateEvent>
}