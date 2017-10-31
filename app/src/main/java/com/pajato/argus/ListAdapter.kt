package com.pajato.argus

import android.graphics.Color
import android.graphics.PorterDuff
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
        val titleManager = PersistEditedData(titleTextView)
        titleTextView.setOnEditorActionListener(titleManager)
        titleTextView.onFocusChangeListener = titleManager

        // Do the same for the network view.
        val networkTextView = holder.layout.networkText
        networkTextView.setText(items[position].network)
        val networkManager = PersistEditedData(networkTextView)
        networkTextView.setOnEditorActionListener(networkManager)
        networkTextView.onFocusChangeListener = networkManager

        val networks = holder.layout.context.resources.getStringArray(R.array.networks).toList()
        val id = android.R.layout.simple_dropdown_item_1line
        networkTextView.setAdapter(ArrayAdapter<String>(holder.layout.context, id, networks))

        // Set an onClick and color the delete button.
        val deleteButton = holder.layout.deleteButton
        deleteButton.setOnClickListener(Delete(holder, this))
        deleteButton.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(video: Video) {
        items.add(video)
        this.notifyItemInserted(items.size - 1)
    }

    fun removeItem(position : Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    // The ViewHolder class
    class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)
}