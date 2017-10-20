package com.pajato.argus

import android.support.v7.widget.AppCompatImageButton
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
        val titleTextView = holder.layout.findViewWithTag<TextView>("title")
        titleTextView.text = items[position].title
        val networkTextView = holder.layout.findViewWithTag<TextView>("network")
        networkTextView.text = items[position].network
        val deleteButton = holder.layout.findViewWithTag<AppCompatImageButton>("delete")
        deleteButton.setOnClickListener(Delete(holder, this))
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

    class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)
    class Delete(private val holder: ViewHolder, private val adapter: ListAdapter) : View.OnClickListener {
        override fun onClick(v: View?) {
            DatabaseHelper.deleteVideo(adapter.items[holder.adapterPosition], holder.layout.context)
            adapter.removeItem(holder.adapterPosition)
        }
    }
}