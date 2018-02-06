package com.pajato.argus

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.tv_layout.view.*
import kotlinx.android.synthetic.main.video_content.view.*
import kotlinx.android.synthetic.main.video_layout.view.*

class ListAdapter(val items: MutableList<Video>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutCode = if (viewType == TV_SHOW_KEY) R.layout.tv_layout else R.layout.video_layout
        val layout = LayoutInflater.from(parent.context).inflate(layoutCode, parent, false)
        return ViewHolder(layout)
    }

    /** Add an array of listeners to various views in the ViewHolder. */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Fill in the title data and set up some input event listeners.
        val titleTextView = holder.layout.titleText
        titleTextView.setText(items[position].title)
        val titleManager = EditorHelper(titleTextView, holder.layout)
        titleTextView.addTextChangedListener(titleManager)

        // Do the same for the network view.
        val networkTextView = holder.layout.networkText
        networkTextView.setText(items[position].network)
        val networkManager = EditorHelper(networkTextView, holder.layout)
        networkTextView.addTextChangedListener(networkManager)

        val networks = holder.layout.context.resources.getStringArray(R.array.networks).toList()
        val id = android.R.layout.simple_dropdown_item_1line
        networkTextView.setAdapter(ArrayAdapter<String>(holder.layout.context, id, networks))

        // Set an onClick and color the delete button.
        val deleteButton = holder.layout.findViewById<AppCompatImageView>(R.id.deleteButton)
        deleteButton.setOnClickListener(Delete(holder.adapterPosition))
        deleteButton.setColorFilter(Color.GRAY)
        val cardView = holder.layout.findViewById<CardView>(R.id.card_view)
        cardView.setOnTouchListener(TakeFocus())

        // Finish binding different view types separately.
        when (getItemViewType(position)) {
            VIDEO_KEY -> bindVideo(holder, position)
            TV_SHOW_KEY -> {
                bindVideo(holder, position)
                bindTvShow(holder, position)
            }
        }
    }

    /** Get the total number of items in the adapter. */
    override fun getItemCount(): Int {
        return items.size
    }

    /** Items will either be episodic or standard videos. */
    override fun getItemViewType(position: Int): Int {
        val v = items[position]
        return if (v is Episodic) {
            TV_SHOW_KEY
        } else {
            VIDEO_KEY
        }
    }

    /** Add an item to the adapter. */
    fun addItem(video: Video) {
        items.add(video)
        this.notifyItemInserted(items.size - 1)
    }

    /** Remove an item from the adapter. */
    fun removeItem(position: Int): Video {
        val v = items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
        return v
    }

    /** Standard videos have some listeners that should be added. */
    private fun bindVideo(holder: ViewHolder, position: Int) {
        // Populate the date text and set calendar choice onClicks
        val dateTextView = holder.layout.dateText
        val viewedEye = holder.layout.findViewById<AppCompatImageView>(R.id.viewedEye)
        dateTextView?.text = items[position].dateWatched
        val dateButton = holder.layout.findViewById<AppCompatImageView>(R.id.dateButton)

        // Depending on if the dateWatched property exists, display it or not.
        if (items[position].dateWatched != "") {
            dateButton.setColorFilter(ContextCompat.getColor(holder.layout.context, R.color.colorAccent))
            viewedEye?.visibility = View.VISIBLE
            viewedEye?.setColorFilter(Color.GRAY)
        } else {
            dateButton.setColorFilter(Color.GRAY)
            viewedEye?.visibility = View.INVISIBLE
        }
        holder.layout.locationText?.text = items[position].locationWatched

        // Set up the recording onClick to the various views that should expect to be clicked on.
        val recordDateOnClick = RecordDate(holder.adapterPosition)
        dateButton.setOnClickListener(recordDateOnClick)
        dateTextView?.setOnClickListener(recordDateOnClick)
        viewedEye?.setOnClickListener(recordDateOnClick)
    }

    /** Episodic videos have specific additional listeners that need to be added. */
    private fun bindTvShow(holder: ViewHolder, position: Int) {
        // Populate the season tracker and assign it an onclick.
        val seasonLabel = holder.layout.seasonLabel
        val seasonTracker = EpisodeTracker(position)
        seasonLabel.setOnClickListener(seasonTracker)
        seasonLabel.setOnLongClickListener(seasonTracker)
        holder.layout.seasonText.text = (items[position] as? Episodic)?.season?.toString() ?: "1"

        // Populate the episode tracker and assign it an onClick.
        val episodeLabel = holder.layout.episodeLabel
        val episodeTracker = EpisodeTracker(position)
        episodeLabel.setOnClickListener(episodeTracker)
        episodeLabel.setOnLongClickListener(episodeTracker)
        holder.layout.episodeText.text = (items[position] as? Episodic)?.episode?.toString() ?: "1"
    }

    // The ViewHolder class
    class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    companion object {
        val VIDEO_KEY = 0
        val TV_SHOW_KEY = 1
    }
}
