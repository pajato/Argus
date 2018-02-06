package com.pajato.argus

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.pajato.argus.event.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.dialog_content.view.*
import kotlinx.android.synthetic.main.non_empty_list_content_main.*
import kotlinx.android.synthetic.main.tv_layout.view.*
import kotlinx.android.synthetic.main.video_content.view.*
import kotlinx.android.synthetic.main.video_layout.view.*
import java.lang.Integer.parseInt

object RecyclerViewHolderManager : Event.EventListener {

    private lateinit var activity: MainActivity
    private var subs: List<Disposable> = emptyList()

    /** Subscribe to events and obtain the instance of the MainActivity. */
    fun init(activity: MainActivity) {
        this.activity = activity
        subs = listOf(RxBus.subscribeToEventType(DeleteEvent::class.java, this),
                RxBus.subscribeToEventType(LocationEvent::class.java, this),
                RxBus.subscribeToEventType(WatchedEvent::class.java, this),
                RxBus.subscribeToEventType(SeasonEvent::class.java, this),
                RxBus.subscribeToEventType(EpisodeEvent::class.java, this))
    }

    /** Handle events differently depending on their class. */
    override fun accept(event: Event) {
        when (event) {
            is DeleteEvent -> handleDeleteEvent(event)
            is LocationEvent -> handleLocationEvent(event)
            is WatchedEvent -> handleWatchedEvent(event)
            is SeasonEvent -> handleSeasonEvent(event)
            is EpisodeEvent -> handleEpisodeEvent(event)
        }
    }

    /** Delete events remove the video from the layout and remove it from the database. */
    private fun handleDeleteEvent(event: DeleteEvent) {
        val position = event.getData()
        val v = (activity.listItems.adapter as ListAdapter).removeItem(position)
        deleteVideo(v, activity)
    }

    /** Season events update a particular episodic video's current season. */
    private fun handleSeasonEvent(event: SeasonEvent) {
        val position = event.getData()
        val layout = activity.listItems.findViewHolderForAdapterPosition(position).itemView
        val title = layout.titleText.text.toString()
        val textView = layout.seasonText
        val field = "season"

        if (event.increment) {
            incrementSeasonOrEpisode(textView, title)
        } else {
            updateSeasonOrEpisode(layout, textView, field, field)
        }
    }

    /** Episode events update a particular episodic video's current episode. */
    private fun handleEpisodeEvent(event: EpisodeEvent) {
        val position = event.getData()
        val layout = activity.listItems.findViewHolderForAdapterPosition(position).itemView
        val title = layout.titleText.text.toString()
        val textView = layout.episodeText
        val field = "episode"
        val message = "$field of season " + layout.seasonText.text.toString()

        if (event.increment) {
            incrementSeasonOrEpisode(textView, title)
        } else {
            updateSeasonOrEpisode(layout, textView, field, message)
        }
    }

    /** Location Events update the layout according to the new location, and update the database. */
    private fun handleLocationEvent(event: LocationEvent) {
        val position = event.getData()
        val layout = activity.listItems.findViewHolderForAdapterPosition(position).itemView
        val locationWatched: String = event.getLocation()
        layout.locationText?.text = locationWatched

        // Get the video's full information from the layout and update its entry in the database.
        val contentValues = ContentValues()
        contentValues.put(DatabaseEntry.COLUMN_NAME_LOCATION_WATCHED, locationWatched)
        updateVideoValues(layout.titleText.text.toString(), contentValues, activity)
    }

    /** Watched Events should update the database. and then update the layout. */
    private fun handleWatchedEvent(event: WatchedEvent) {
        val position = event.getData()
        val layout = activity.listItems.findViewHolderForLayoutPosition(position).itemView

        val contentValues = ContentValues()
        contentValues.put(DatabaseEntry.COLUMN_NAME_DATE_WATCHED, event.getDateWatched())
        updateVideoValues(layout.titleText.text.toString(), contentValues, activity)

        layout.dateText?.text = event.getDateWatched()
        layout.findViewById<AppCompatImageView>(R.id.viewedEye)?.visibility = View.VISIBLE
        layout.findViewById<AppCompatImageView>(R.id.viewedEye)?.setColorFilter(Color.GRAY)
        layout.findViewById<AppCompatImageView>(R.id.dateButton)
                .setColorFilter(ContextCompat.getColor(layout.context, R.color.colorAccent))
    }

    /** Increment the season or episode value by one, then update the database. */
    private fun incrementSeasonOrEpisode(textView: TextView, title: String) {
        var number = parseInt(textView.text.toString())
        number += 1
        textView.text = number.toString()

        val contentValues = ContentValues()
        val c = if (textView.id == R.id.seasonText) DatabaseEntry.COLUMN_NAME_SEASON else DatabaseEntry.COLUMN_NAME_EPISODE
        contentValues.put(c, number)
        updateVideoValues(title, contentValues, activity)
    }

    /** Set the season or episode value to a value as entered in an alert by the user.*/
    @SuppressLint("InflateParams")
    private fun updateSeasonOrEpisode(layout: View, textView: TextView, field: String, message: String) {
        // Construct an alert and prepare relevant information for our custom layout.
        val builder = AlertDialog.Builder(layout.context)
        val title = layout.titleText.text.toString()
        val alertTitle = title.capitalize() + " " + field.capitalize()
        val alertMessage = "Set the $message of $title that you have most recently watched."

        // In spite of the lint warning, this is the recommended way to add a custom view to an alert builder.
        val content = activity.layoutInflater.inflate(R.layout.dialog_content, null)
        content.alertTitle.text = alertTitle
        content.alertMessage.text = alertMessage
        builder.setView(content)
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                .setPositiveButton("OK") { _, _ ->
                    val value = content.alertInput.text.toString()
                    textView.text = value

                    val contentValues = ContentValues()
                    val c = if (textView.id == R.id.seasonText) DatabaseEntry.COLUMN_NAME_SEASON else DatabaseEntry.COLUMN_NAME_EPISODE
                    contentValues.put(c, parseInt(value))
                    updateVideoValues(title, contentValues, activity)
                }

        builder.show()
    }

    /** Unsubscribe from events. */
    fun destroy() {
        subs.forEachIndexed { _, disposable -> disposable.dispose() }
        subs = emptyList()
    }
}
