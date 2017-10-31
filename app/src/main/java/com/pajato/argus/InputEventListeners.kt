package com.pajato.argus

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


// An onClick that deletes the item from the adapter and removes it from the database.
class Delete(private val holder: ListAdapter.ViewHolder, private val adapter: ListAdapter) : View.OnClickListener {
    override fun onClick(v: View?) {
        deleteVideo(adapter.items[holder.adapterPosition], holder.layout.context)
        adapter.removeItem(holder.adapterPosition)
    }
}

class PersistEditedData(editText: EditText) : TextView.OnEditorActionListener, View.OnFocusChangeListener {

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {

            Log.v(this.javaClass.toString(), "onEditorAction: IME_ACTION_DONE for view "
                    + v.javaClass.canonicalName.toString() + " with id " + v.id.toString())
            v.clearFocus()

            val manager: InputMethodManager? = v.context.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            manager?.hideSoftInputFromWindow(v.windowToken, 0)

            return true
        }
        return false
    }

    private var s: String = editText.text.toString()

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        Log.v(this.javaClass.toString(), "OnFocusChange for view "
                + v.javaClass.canonicalName.toString() + " with id " + v.id.toString()
                + " has focus: " + hasFocus)
        if (v !is EditText) return
        if (!hasFocus) {
            v.isCursorVisible = false
            val title = (v.parent as? View?)?.findViewById<EditText>(R.id.titleText)?.text.toString()
            val network = (v.parent as? View?)?.findViewById<AutoCompleteTextView>(R.id.networkText)?.text.toString()
            val video = Video(title, network)
            updateVideo(s, video, v.context)
        } else {
            v.isCursorVisible = true
            s = v.text.toString()
        }
    }
}