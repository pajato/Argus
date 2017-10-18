package com.pajato.argus

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_search.*

@ContainerOptions(CacheImplementation.NO_CACHE)
class SearchActivity : AppCompatActivity() {

    companion object {
        val TYPE_KEY = "type"
        val TITLE_KEY = "title"
        val NETWORK_KEY = "network"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Create the activity by establishing the layout, the toolbar and putting text watchers on
        // the edit text boxes so that the save button can enabled/disabled appropriately.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(searchToolbar)
        searchName.afterTextChanged { processSaveButtonState() }
        network.afterTextChanged { processSaveButtonState() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu, color the check icon to be the correct color and disable it.
        menuInflater.inflate(R.menu.save_menu, menu)
        menu.getItem(0).isEnabled = false
        val color = ContextCompat.getColor(applicationContext, android.R.color.white)
        menu.getItem(0).icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle a click on the save button by wrapping up the activity.
        if (item.itemId == R.id.save_button) {
            save()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        // Add the auto-fill resources to the networks input and set the focus on the search name
        // edit text widget (and cause the soft keyboard to display if there is no hard keyboard).
        super.onResume()
        val networks = resources.getStringArray(R.array.networks).toList()
        val id = android.R.layout.simple_dropdown_item_1line
        network.setAdapter(ArrayAdapter<String>(this, id, networks))
        searchName.requestFocus()
    }

    private fun save() {
        // Return the collected data to the main activity.
        val result = Intent()
        result.putExtra(TYPE_KEY, "video")
        result.putExtra(TITLE_KEY, searchName.editableText.toString())
        result.putExtra(NETWORK_KEY, network.editableText.toString())
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        // Establish an edit text extension function to deal with the save button.
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    private fun processSaveButtonState() {
        val menu = searchToolbar.menu
        menu.getItem(0).isEnabled = !(searchName.text.isEmpty() || network.text.isEmpty())
    }
}
