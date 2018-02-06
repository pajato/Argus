package com.pajato.argus.event

import io.reactivex.functions.Consumer

/**
 * An event used to transmit changes to the Location Watched field for videos.
 * @param position the position of the video in the RecyclerView's adapter.
 */
class LocationPermissionEvent(private val position: Int) : Event {
    override fun getData(): Int {
        return position
    }

    interface LocationEventListener : Consumer<LocationPermissionEvent>
}
