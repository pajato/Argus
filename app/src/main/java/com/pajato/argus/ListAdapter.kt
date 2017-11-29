package com.pajato.argus

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.video_layout.view.*

class ListAdapter(val items: MutableList<Video>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.video_layout, parent, false)
        return ViewHolder(layout)
    }

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
        val deleteButton = holder.layout.deleteButton
        deleteButton.setOnClickListener(Delete(holder, this))
        deleteButton.setColorFilter(Color.GRAY)
        holder.layout.card_view.setOnTouchListener(TakeFocus())

        // Populate the date text and set calendar choice onClicks
        val dateTextView = holder.layout.dateText
        dateTextView.text = items[position].dateWatched
        if (items[position].dateWatched != "") {
            holder.layout.dateButton.setColorFilter(ContextCompat.getColor(holder.layout.context, R.color.colorAccent))
            holder.layout.viewedEye.visibility = View.VISIBLE
            holder.layout.viewedEye.setColorFilter(Color.GRAY)
        } else {
            holder.layout.dateButton.setColorFilter(Color.GRAY)
            holder.layout.viewedEye.visibility = View.INVISIBLE
        }

        val recordDateOnClick = RecordDate(holder.layout.card_view)
        holder.layout.dateButton.setOnClickListener(recordDateOnClick)
        dateTextView.setOnClickListener(recordDateOnClick)
        holder.layout.viewedEye.setOnClickListener(recordDateOnClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(video: Video) {
        items.add(video)
        this.notifyItemInserted(items.size - 1)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    // The ViewHolder class
    class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)
}