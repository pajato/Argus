package com.pajato.argus.event

import io.reactivex.functions.Consumer

/**
 * An event used to transmit changes to the Location Watched field for videos.
 * @param position the position of the video in the RecyclerView's adapter.
 */
class LocationEvent(private val position: Int) : Event {
    constructor(position: Int, location: String) : this(position) {
        this.setLocation(location)
    }

    private var locationWatched: String = ""

    override fun getData(): Int {
        return position
    }

    fun getLocation(): String {
        return this.locationWatched
    }

    fun setLocation(location: String) {
        this.locationWatched = location
    }

    interface LocationConfirmedListener : Consumer<LocationEvent>
}