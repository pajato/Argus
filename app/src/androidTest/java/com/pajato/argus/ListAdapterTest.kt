package com.pajato.argus

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.non_empty_list_content_main.*
import org.junit.Assert.assertEquals
import org.junit.Test


class ListAdapterTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {
    @Test
    fun testInitialState() {
        // Assert that the adapter is empty on start.
        val list: RecyclerView = rule.activity.listItems
        val adapter: ListAdapter = list.adapter as ListAdapter
        val message = "The adapter does not have the correct number of elements!"
        assertEquals(message, 0, adapter.itemCount)
    }

    @Test
    fun testEmptyListToFullList() {
        // Setup the Data to enter into the adapter.
        val list: RecyclerView = rule.activity.listItems
        val adapter: ListAdapter = list.adapter as ListAdapter
        val videoName = "Luther"
        val network = "HBO Now"
        val type = "video"
        onView(withId(R.id.fab)).perform(click())

        // Utilize espresso to wait for the activity to appear, then enter our data
        checkViewVisibility(withId(R.id.save_button), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.searchName)).perform(replaceText(videoName))
        onView(withId(R.id.network)).perform(replaceText(network))
        onView(withId(R.id.save_button)).perform(click())

        // Test that the ActivityResult has interacted with the MainActivity & the data is displayed
        checkViewVisibility(withId(R.id.emptyListFrame), ViewMatchers.Visibility.GONE)
        checkViewVisibility(ViewMatchers.withId(R.id.listItems), ViewMatchers.Visibility.VISIBLE)
        assertEquals(adapter.itemCount, 1)
        val video = adapter.items[0]
        assertEquals(video.title, videoName)
        assertEquals(video.network, network)
        assertEquals(video.type, type)
    }

}