package com.pajato.argus

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import com.pajato.argus.event.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.non_empty_list_content_main.*
import kotlinx.android.synthetic.main.video_layout.view.*

object RecyclerViewHolderManager : Event.EventListener {

    private lateinit var activity: MainActivity
    private var subs: List<Disposable> = emptyList()

    fun init(activity: MainActivity) {
        this.activity = activity
        subs = listOf(RxBus.subscribeToEventType(DeleteEvent::class.java, this),
                RxBus.subscribeToEventType(LocationEvent::class.java, this),
                RxBus.subscribeToEventType(WatchedEvent::class.java, this))
    }

    override fun accept(event: Event) {
        when (event) {
            is DeleteEvent -> handleDeleteEvent(event)
            is LocationEvent -> handleLocationEvent(event)
            is WatchedEvent -> handleWatchedEvent(event)
        }
    }

    private fun handleDeleteEvent(event: DeleteEvent) {
        val position = event.getData()
        val v = (activity.listItems.adapter!! as ListAdapter).removeItem(position)
        deleteVideo(v, activity)
    }

    private fun handleLocationEvent(event: LocationEvent) {
        val position = event.getData()
        if (position == -1)
            return
        val layout = activity.listItems.getChildAt(position)
        val locationWatched: String = event.getLocation()
        layout.locationText.text = locationWatched

        val title = layout.titleText.text.toString()
        val network = layout.networkText.text.toString()
        val dateWatched = layout.dateText.text.toString()

        val v = Video(title, network, dateWatched, "", locationWatched)
        updateVideo(v.title, v, activity)
    }

    private fun handleWatchedEvent(event: WatchedEvent) {
        val position = event.getData()
        val layout = activity.listItems.getChildAt(position)

        layout.dateText.text = event.getDateWatched()
        val title = layout.titleText.text.toString()
        val network = layout.networkText.text.toString()
        val v = Video(title, network, event.getDateWatched())
        updateVideo(title, v, layout.context)

        layout.viewedEye.visibility = View.VISIBLE
        layout.viewedEye.setColorFilter(Color.GRAY)
        layout.dateButton.setColorFilter(ContextCompat.getColor(layout.context, R.color.colorAccent))
    }

    fun destroy() {
        subs.forEachIndexed { _, disposable -> disposable.dispose() }
        subs = emptyList()
    }
}