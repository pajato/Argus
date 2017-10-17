package com.pajato.argus

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ListAdapter(val items: MutableList<Video>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.video_layout, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val titleTextView: TextView = holder.layout.findViewWithTag("title")
        titleTextView.text = items[position].title
        val networkTextView: TextView = holder.layout.findViewWithTag("network")
        networkTextView.text = items[position].network
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(video : Video) {
        items.add(video)
        this.notifyItemInserted(items.size - 1)
    }

    class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)
}