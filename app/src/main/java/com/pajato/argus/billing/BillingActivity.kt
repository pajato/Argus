package com.pajato.argus.billing

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.pajato.argus.R
import com.pajato.argus.event.Event
import com.pajato.argus.event.PurchaseUpdateEvent
import com.pajato.argus.event.RxBus
import com.pajato.argus.event.UserCancelledBillingEvent
import io.reactivex.disposables.Disposable

class BillingActivity : AppCompatActivity(), Event.EventListener {
    private var subs: List<Disposable> = emptyList()

    /** Accept billing events that end the billing process. */
    override fun accept(event: Event) {
        if (event is PurchaseUpdateEvent) {
            val purchase = event.getData()
            if (purchase.sku == BillingManager.MULTIPLE_DEVICE_SUPPORT_KEY) {
                finish()
            }
        } else if (event is UserCancelledBillingEvent) {
            finish()
        }
    }

    /** Initialize the billing manager and event listeners. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_billing)
        BillingManager.destroy()
        BillingManager.init(this)

        subs = listOf(RxBus.subscribeToEventType(PurchaseUpdateEvent::class.java, this),
                RxBus.subscribeToEventType(UserCancelledBillingEvent::class.java, this))
        // TODO: Check if subscription already purchased?
    }

    fun subscribe(view: View) {
        BillingManager.subscribe(BillingManager.MULTIPLE_DEVICE_SUPPORT_KEY, this)
    }

    override fun finish() {
        BillingManager.destroy()

        subs.forEachIndexed { _, disposable -> disposable.dispose() }
        subs = emptyList()

        super.finish()
    }

}
