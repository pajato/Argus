package com.pajato.argus.event

import io.reactivex.functions.Consumer

/**
 * An event object used to transmit changes to the Episode data in Episodic videos.
 * @param position the position of the video in the RecyclerView's adapter.
 */
class EpisodeEvent(private val position: Int) : Event {
    var increment: Boolean = false

    constructor(position: Int, increment: Boolean) : this(position) {
        this.increment = increment
    }

    override fun getData(): Int {
        return position
    }

    interface EpisodeEventListener : Consumer<EpisodeEvent>
}
