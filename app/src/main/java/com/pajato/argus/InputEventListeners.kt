package com.pajato.argus

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager


// An onClick that deletes the item from the adapter and removes it from the database.
class Delete(private val holder: ListAdapter.ViewHolder, private val adapter: ListAdapter) : View.OnClickListener {
    override fun onClick(v: View?) {
        deleteVideo(adapter.items[holder.adapterPosition], holder.layout.context)
        adapter.removeItem(holder.adapterPosition)
    }
}

// Handle editing videos by tracking text inputs.
class EditorHelper(private val editText: EditText, private val parent: View): OnEditorActionListener, TextWatcher {
    private var previousTitle: String = ""
    private var previousNetwork: String = ""

    // After the text is changed, update the database.
    override fun afterTextChanged(s: Editable?) {
        if (editText.id == R.id.titleText) {
            updateVideo(previousTitle, Video(s.toString(), previousNetwork), editText.context)
        } else {
            updateVideo(previousTitle, Video(previousTitle, s.toString()), editText.context)
        }
    }

    // Before the text is changed, save the previous information.
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (editText.id == R.id.titleText) {
            previousTitle = s.toString()
            previousNetwork = parent.findViewById<EditText>(R.id.networkText).text.toString()
        } else {
            previousNetwork = s.toString()
            previousTitle = parent.findViewById<EditText>(R.id.titleText).text.toString()
        }
    }

    // While it is required we implement this method by the TextWatcher class, we do not need it.
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    // Clear focus to disappear the text marker and ensure the soft keyboard is dismissed when Done
    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            v.clearFocus()
            (v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(v.windowToken, 0)
            return true
        }
        return false
    }
}