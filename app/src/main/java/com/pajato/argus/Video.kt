package com.pajato.argus

class Video(val title: String, val network: String) {
    var type: String = ""
    var dateWatched: String = ""
    var locationWatched: String = ""

    constructor(title: String, network: String, dateWatched: String, type: String = "") : this(title, network) {
        this.type = type
        this.dateWatched = dateWatched
    }

    constructor(title: String, network: String, dateWatched: String, type: String, locationWatched: String) :
            this(title, network, dateWatched, type) {
        this.locationWatched = locationWatched
    }
}