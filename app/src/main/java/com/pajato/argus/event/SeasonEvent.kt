package com.pajato.argus.event

import io.reactivex.functions.Consumer

/**
 * An event object used to transmit changes to the Season field for Episodic videos.
 * @param position the position of the event in the RecyclerView's adapter.
 */
class SeasonEvent(private val position: Int) : Event {
    var increment: Boolean = false

    constructor(position: Int, increment: Boolean) : this(position) {
        this.increment = increment
    }

    override fun getData(): Int {
        return position
    }

    interface SeasonEventListener : Consumer<SeasonEvent>
}