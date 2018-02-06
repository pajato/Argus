package com.pajato.argus

/** A relatively simple POJO that stores the relevant data for each video to our database. */
open class Video(val title: String, val network: String) {
    var type: String = MOVIE_KEY
    var dateWatched: String = ""
    var locationWatched: String = ""

    constructor(title: String, network: String, type: String) : this(title, network) {
        this.type = type
    }

    constructor(title: String, network: String, type: String, dateWatched: String, locationWatched: String) : this(title, network) {
        this.type = type
        this.dateWatched = dateWatched
        this.locationWatched = locationWatched
    }

    companion object {
        val TV_KEY = "tvShowKey"
        val MOVIE_KEY = "movieKey"
    }
}

/** A subset of the Video class that also tracks episodes and seasons. */
class Episodic(title: String, network: String) : Video(title, network, Video.TV_KEY) {
    var season: Int = 1
    var episode: Int = 1

    constructor(title: String, network: String, season: Int, episode: Int) : this(title, network) {
        this.season = season
        this.episode = episode
    }

    constructor(title: String, network: String, season: Int, episode: Int, dateWatched: String,
                locationWatched: String) : this(title, network, season, episode) {
        this.dateWatched = dateWatched
        this.locationWatched = locationWatched
    }

}
