package com.pajato.argus

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.non_empty_list_content_main.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListAdapterTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

    @Test fun testInitialState() {
        // Assert that the adapter is empty on start.
        val list: RecyclerView = rule.activity.listItems
        val adapter: ListAdapter = list.adapter as ListAdapter
        val message = "The adapter does not have the correct number of elements!"
        assertEquals(message, 0, adapter.itemCount)
    }

    @Test fun testEmptyListToFullList() {
        // Setup the Data to enter into the adapter.
        val list: RecyclerView = rule.activity.listItems
        val adapter: ListAdapter = list.adapter as ListAdapter
        val videoName = "Luther"
        val network = "HBO Now"
        onView(withId(R.id.fab)).perform(click())

        // Utilize espresso to wait for the activity to appear, then enter our data
        checkViewVisibility(withId(R.id.save_button), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.searchName)).perform(replaceText(videoName))
        onView(withId(R.id.network)).perform(replaceText(network))
        onView(withId(R.id.save_button)).perform(click())

        // Test that the ActivityResult has interacted with the MainActivity & the data is displayed
        checkViewVisibility(withId(R.id.emptyListFrame), ViewMatchers.Visibility.GONE)
        checkViewVisibility(ViewMatchers.withId(R.id.listItems), ViewMatchers.Visibility.VISIBLE)
        assertEquals("Adapter has wrong count", 1, adapter.itemCount)
        val video = adapter.items[0]
        assertEquals(videoName, video.title)
        assertEquals(network, video.network)
        assertEquals(Video.MOVIE_KEY, video.type)
    }

    @Test fun testNullAdapter() {
        // Perform a click on the FAB to cause data to be entered into the search activity fields
        // and ensure that the activity result callback finds the adapter to be null, thus ensuring
        // 100% coverage.
        val list: RecyclerView = rule.activity.listItems
        list.adapter = null
        onView(withId(R.id.fab)).perform(click())
        checkViewVisibility(withId(R.id.save_button), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.searchName)).perform(replaceText("Luther"))
        onView(withId(R.id.network)).perform(replaceText("HBO Now"))
        onView(withId(R.id.save_button)).perform(click())
        assertTrue("The adapter is not null!", list.adapter == null)
    }
}
