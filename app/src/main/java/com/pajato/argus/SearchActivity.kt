package com.pajato.argus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
    }

    fun save(view: View) {

        if (!name.text.isEmpty()) {
            val result = Intent()
            result.putExtra(TYPE_KEY, "video")
            result.putExtra(TITLE_KEY, name.editableText.toString())
            result.putExtra(NETWORK_KEY, network.selectedItem.toString())
            setResult(Activity.RESULT_OK, result)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }

}
