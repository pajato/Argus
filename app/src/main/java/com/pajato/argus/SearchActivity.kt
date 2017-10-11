package com.pajato.argus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        val TYPE_KEY = "type"
        val TITLE_KEY = "title"
        val NETWORK_KEY = "network"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu and color the check icon to be the correct color
        menuInflater.inflate(R.menu.save_menu, menu)
        val drawable = menu.getItem(0).icon
        drawable.mutate()
        val color = ContextCompat.getColor(applicationContext, android.R.color.white)
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle a click on the save button by wrapping up the activity.
        item?.apply {
            if (item.itemId == R.id.save_button) {
                save()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        // Add the auto-fill resources to the networks input.
        val networks = resources.getStringArray(R.array.networks).toList()
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, networks)
        network.setAdapter(arrayAdapter)

        // Force the activity to focus on the name input
        name.requestFocus()
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun save() {
        if (!name.text.isEmpty()) {
            val result = Intent()
            result.putExtra(TYPE_KEY, "video")
            result.putExtra(TITLE_KEY, name.editableText.toString())
            result.putExtra(NETWORK_KEY, network.editableText.toString())
            setResult(Activity.RESULT_OK, result)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
        finish()
    }

}
