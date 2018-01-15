package com.pajato.argus.event

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

object RxBus {
    private val bus: PublishSubject<Event> = PublishSubject.create<Event>()
    fun <T : Event> send(event: T) {
        bus.onNext(event)
    }

    fun <T : Event> subscribeToEventType(c: Class<T>, listener: Consumer<in T>): Disposable {
        return bus.ofType(c).subscribe(listener)
    }
}