package com.pajato.argus

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.AppCompatImageView
import kotlinx.android.synthetic.main.non_empty_list_content_main.*
import org.hamcrest.Matchers
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert
import org.junit.Test

class DatabaseTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {
    @Test
    fun testPersistedData() {
        // Load in two videos of Data
        rule.activity.runOnUiThread {
            var video = Video("Luther", "HBO Go")
            rule.activity.addVideo(video)
            video = Video("Boardwalk Empire", "HBO Now")
            rule.activity.addVideo(video)
        }

        // Ensure that the videos' data is present, then do a lifecycle
        checkViewVisibility(ViewMatchers.withId(R.id.listItems), Visibility.VISIBLE)
        Assert.assertEquals("Adapter has wrong count before lifecycles", 2, rule.activity.listItems.adapter.itemCount)
        doLifeCycle()

        // Ensure that the data is present post lifecycle, then delete one and ensure it's deleted.
        checkViewVisibility(ViewMatchers.withId(R.id.listItems), Visibility.VISIBLE)
        Assert.assertEquals("Adapter has wrong count after first lifecycle", 2, rule.activity.listItems.adapter.itemCount)
        onView(Matchers.allOf(instanceOf(AppCompatImageView::class.java), hasSibling(withText("Luther")),
                hasSibling(ViewMatchers.withText("HBO Go")),
                withId(R.id.deleteButton)))
                .perform(click())
        checkViewVisibility(withId(R.id.listItems), Visibility.VISIBLE)
        Assert.assertEquals("Adapter has wrong count after deleting", 1, rule.activity.listItems.adapter.itemCount)

        // Lifecycle again, then ensure that only one data is left after lifecycling a final time.
        doLifeCycle()
        checkViewVisibility(ViewMatchers.withId(R.id.listItems), Visibility.VISIBLE)
        Assert.assertEquals("Adapter has wrong count after second lifecycle", 1, rule.activity.listItems.adapter.itemCount)

    }

    @Test
    fun testEditedData() {
        rule.activity.runOnUiThread {
            var video = Video("Luther", "HBO Go")
            rule.activity.addVideo(video)
            video = Video("Boardwalk Empire", "HBO Now")
            rule.activity.addVideo(video)
        }

        onView(withText("Boardwalk Empire"))
                .perform(click())
                .perform(ViewActions.replaceText("The 100"))
        onView(withText("The 100"))
                .perform(ViewActions.pressImeActionButton())

        onView(withText("HBO Now"))
                .perform(ViewActions.replaceText("Netflix"))
        onView(withText("Netflix"))
                .perform(ViewActions.pressImeActionButton())

        checkViewVisibility(ViewMatchers.withId(R.id.listItems), Visibility.VISIBLE)
        Assert.assertEquals("Adapter has wrong count before lifecycles", 2, rule.activity.listItems.adapter.itemCount)
        doLifeCycle()

        checkViewVisibility(ViewMatchers.withId(R.id.listItems), Visibility.VISIBLE)
        Assert.assertEquals("Adapter has wrong count after first lifecycle", 2, rule.activity.listItems.adapter.itemCount)
        onView(withText("The 100"))
                .check(matches(isDisplayed()))
                .perform(click())
    }
}