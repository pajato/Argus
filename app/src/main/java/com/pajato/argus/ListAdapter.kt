package com.pajato.argus

import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView

class ListAdapter(val items: MutableList<Video>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.video_layout))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val titleTextView: TextView = holder.layout.getChildAt(0) as TextView
        titleTextView.text = items[position].title
        val networkTextView: TextView = holder.layout.getChildAt(1) as TextView
        networkTextView.text = items[position].network
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(video : Video) {
        items.add(video)
        this.notifyItemInserted(items.size - 1)
    }

    class ViewHolder(val layout: LinearLayoutCompat) : RecyclerView.ViewHolder(layout)
}