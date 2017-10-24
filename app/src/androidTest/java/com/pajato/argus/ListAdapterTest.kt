package com.pajato.argus

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBackUnconditionally
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.non_empty_list_content_main.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
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
        assertEquals("Adapter has wrong count", 1, adapter.itemCount)
        val video = adapter.items[0]
        assertEquals(video.title, videoName)
        assertEquals(video.network, network)
        assertEquals(video.type, type)
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

    @Test fun testPersistedData() {
        // Load in two videos of Data
        rule.activity.runOnUiThread {
            var video = Video("Luther", "HBO Go")
            rule.activity.addVideo(video)
            video = Video("Boardwalk Empire", "HBO Now")
            rule.activity.addVideo(video)
        }

        // Ensure that the videos' data is present, then do a lifecycle
        checkViewVisibility(withId(R.id.listItems), ViewMatchers.Visibility.VISIBLE)
        assertEquals("Adapter has wrong count before lifecycles", 2, rule.activity.listItems.adapter.itemCount)
        doLifeCycle()

        // Ensure that the data is present post lifecycle, then delete one and ensure it's deleted.
        checkViewVisibility(withId(R.id.listItems), ViewMatchers.Visibility.VISIBLE)
        assertEquals("Adapter has wrong count after first lifecycle", 2, rule.activity.listItems.adapter.itemCount)
        onView(allOf(instanceOf(AppCompatImageView::class.java), hasSibling(withText("Luther")),
                hasSibling(withText("HBO Go"))))
                .perform(click())
        checkViewVisibility(withId(R.id.listItems), ViewMatchers.Visibility.VISIBLE)
        assertEquals("Adapter has wrong count after deleting", 1, rule.activity.listItems.adapter.itemCount)

        // Lifecycle again, then ensure that only one data is left after lifecycling a final time.
        doLifeCycle()
        checkViewVisibility(withId(R.id.listItems), ViewMatchers.Visibility.VISIBLE)
        assertEquals("Adapter has wrong count after second lifecycle", 1, rule.activity.listItems.adapter.itemCount)

    }

    private fun doLifeCycle(intent: Intent? = null) {
        pressBackUnconditionally()
        Intents.release()

        rule.launchActivity(intent)
    }
}
