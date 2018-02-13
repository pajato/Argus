package com.pajato.argus

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.view.View
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.pajato.argus.database.DatabaseEntry
import com.pajato.argus.database.updateVideoValues
import com.pajato.argus.event.*
import java.text.DateFormat
import java.util.*


/** An onClick that deletes the item from the adapter and removes it from the database. */
class Delete(private val position: Int) : View.OnClickListener {
    override fun onClick(v: View?) {
        RxBus.send(DeleteEvent(position))
    }
}

/** Handle editing videos by tracking text inputs. */
class EditorHelper(private val editText: EditText, private val parent: View) : TextWatcher {
    private var previousTitle: String = ""

    // After the text is changed, update the database.
    override fun afterTextChanged(s: Editable?) {
        val cv = ContentValues()
        val key = if (editText.id == R.id.titleText) DatabaseEntry.COLUMN_NAME_TITLE else DatabaseEntry.COLUMN_NAME_NETWORK
        cv.put(key, editText.text.toString())
        updateVideoValues(previousTitle, cv, editText.context)
    }

    // Before the text is changed, save the previous title's information.
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        previousTitle = if (editText.id == R.id.titleText) {
            s.toString()
        } else {
            parent.findViewById<EditText>(R.id.titleText).text.toString()
        }
    }

    // While it is required we implement this method by the TextWatcher class, we do not need it.
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}

/** An onClick that captures the current date and stores in in the video layout. */
class RecordDate(private val position: Int) : View.OnClickListener {
    override fun onClick(view: View) {
        val date = DateFormat.getDateInstance().format(Date())
        val event = WatchedEvent(position)
        event.setDateWatched(date)
        RxBus.send(event)
        RxBus.send(LocationPermissionEvent(position))
    }
}

/** An onTouchListener that requests focus away from the view that has focus and dismisses the keyboard */
class TakeFocus(private val activity: Activity?) : View.OnTouchListener {
    constructor() : this(null)

    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        var focused = getFocus(v)
        if (activity != null)
            focused = activity.currentFocus

        focused.clearFocus()

        val imm: InputMethodManager = focused.context.getSystemService(Context.INPUT_METHOD_SERVICE)!! as InputMethodManager
        imm.hideSoftInputFromWindow(focused.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        v.requestFocus()
        v.performClick()
        return true
    }

    // We expect only one of three views (per CardView) to have focus in our app,
    private fun getFocus(videoCardView: View): View {
        val n = videoCardView.findViewById<EditText>(R.id.networkText)
        val t = videoCardView.findViewById<EditText>(R.id.titleText)
        return when {
            n.hasFocus() -> n
            t.hasFocus() -> t
            else -> videoCardView
        }
    }
}

/** An OnClick and OnLongClick listener that emits events for Season and Episode changes. */
class EpisodeTracker(private val position: Int) : View.OnClickListener, View.OnLongClickListener {
    override fun onClick(v: View) {
        when (v.id) {
            R.id.seasonLabel -> RxBus.send(SeasonEvent(position, true))
            else -> RxBus.send(EpisodeEvent(position, true))
        }
    }

    override fun onLongClick(v: View): Boolean {
        when (v.id) {
            R.id.seasonLabel -> RxBus.send(SeasonEvent(position))
            R.id.episodeLabel -> RxBus.send(EpisodeEvent(position))
        }
        return true
    }
}
