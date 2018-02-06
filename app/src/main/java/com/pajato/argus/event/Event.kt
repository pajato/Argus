package com.pajato.argus.event

import io.reactivex.functions.Consumer

/** A very simple event interface. Events require a data point, and ideally provide an interface
 * to serve as a listener for it. */
interface Event {
    fun getData(): Any

    interface EventListener : Consumer<Event>
}
