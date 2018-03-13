package com.pajato.argus.event

import io.reactivex.functions.Consumer

class UserCancelledBillingEvent : Event {
    private var data = 0
    override fun getData(): Any {
        return data
    }

    interface UserCancelledBillingEventListener: Consumer<UserCancelledBillingEvent>
}