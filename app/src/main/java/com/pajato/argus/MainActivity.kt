package com.pajato.argus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pajato.argus.SearchActivity.Companion.NETWORK_KEY
import com.pajato.argus.SearchActivity.Companion.TITLE_KEY
import com.pajato.argus.SearchActivity.Companion.TYPE_KEY
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.non_empty_list_content_main.*

@ContainerOptions(CacheImplementation.NO_CACHE)
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Public instance methods.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val type = data.getStringExtra(TYPE_KEY)
            val title = data.getStringExtra(TITLE_KEY)
            val provider = data.getStringExtra(NETWORK_KEY)
            val video = Video(title, provider, type)
            addVideo(video)
        }
    }

    fun addVideo(video: Video) {
        // Add a video to the adapter, and to the database.
        updateLayoutIsEmpty(false)
        val adapter = listItems.adapter
        (adapter as? ListAdapter)?.addItem(video)
        DatabaseHelper.writeVideo(video, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            // Add a new title via an IMDB search.
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, Companion.SEARCH_REQUEST)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Initialize the adapter.
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        listItems.layoutManager = layoutManager
        val adapter = ListAdapter(mutableListOf())
        listItems.adapter = adapter

        // Query the Database and update the adapter.
        val items: MutableList<Video> = DatabaseHelper.getVideosFromDb(applicationContext)
        if (items.isNotEmpty()) {
            updateLayoutIsEmpty(false)
            for (item in items) {
                adapter.addItem(item)
            }
        } else {
            updateLayoutIsEmpty(true)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateLayoutIsEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            emptyListFrame.visibility = View.VISIBLE
            nonEmptyListFrame.visibility = View.GONE
        } else {
            emptyListFrame.visibility = View.GONE
            nonEmptyListFrame.visibility = View.VISIBLE
        }
    }

    companion object {
        /** The search activity request key. */
        private val SEARCH_REQUEST: Int = 1
    }
}
