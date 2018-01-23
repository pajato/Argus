package com.pajato.argus.event

import io.reactivex.functions.Consumer

/**
 * An event object used to specify a particular video to be deleted from both the UI and the database.
 * @param indexDeleted the position of the video to delete in the RecyclerView's adapter.
 */
class DeleteEvent(private val indexDeleted: Int) : Event {
    override fun getData(): Int {
        return indexDeleted
    }

    interface DeleteEventListener : Consumer<DeleteEvent>
}