package com.pajato.argus.event

import io.reactivex.functions.Consumer

interface Event {
    fun getData(): Any

    interface EventListener : Consumer<Event>
}