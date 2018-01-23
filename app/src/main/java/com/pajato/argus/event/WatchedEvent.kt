package com.pajato.argus.event

import io.reactivex.functions.Consumer

/**
 * An event used to transmit changes to the Date Watched field for videos.
 * @param position the position of the video in the RecyclerView's adapter.
 */
class WatchedEvent(private val position: Int) : Event {
    private var timeWatched: String = ""

    override fun getData(): Int {
        return this.position
    }

    fun setDateWatched(time: String) {
        this.timeWatched = time
    }

    fun getDateWatched(): String {
        return this.timeWatched
    }

    interface WatchedEventListener : Consumer<WatchedEvent>
}