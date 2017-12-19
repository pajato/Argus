package com.pajato.argus.event

import io.reactivex.functions.Consumer

interface Event {
    fun getData(): Any

    interface EventListener: Consumer<Event>
}

class WatchedEvent(private val position: Int): Event {
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

    interface WatchedEventListener: Consumer<WatchedEvent>
}

class LocationEvent(private val position: Int): Event {
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

    interface LocationEventListener: Consumer<LocationEvent>
}

class DeleteEvent(private val indexDeleted: Int): Event {
    override fun getData(): Int {
        return indexDeleted
    }

    interface DeleteEventListener: Consumer<DeleteEvent>
}

class LocationPermissionEvent(private val permissionGranted: Boolean): Event {
    override fun getData(): Boolean {
        return permissionGranted
    }

    interface LocationPermissionListener: Consumer<LocationPermissionEvent>
}