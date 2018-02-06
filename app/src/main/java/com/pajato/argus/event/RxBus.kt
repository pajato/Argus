package com.pajato.argus.event

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/** An object that serves as our simple event bus. Events are subscribed to using the
 * subscribeToEventType method, which returns a Disposable that serves as the unsubscription
 * mechanism. To unsubscribe from an event, call the Disposable's dispose() method. */
object RxBus {
    private val bus: PublishSubject<Event> = PublishSubject.create<Event>()

    /** Send an event to the subscribed listeners. */
    fun <T : Event> send(event: T) {
        bus.onNext(event)
    }

    /** Subscribe a listener to an event type. */
    fun <T : Event> subscribeToEventType(c: Class<T>, listener: Consumer<in T>): Disposable {
        return bus.ofType(c).subscribe(listener)
    }
}
