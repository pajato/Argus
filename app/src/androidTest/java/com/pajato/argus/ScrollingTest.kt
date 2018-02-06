package com.pajato.argus

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

/**
 * Tests that scrolling functions properly and does not affect how the card view buttons operate.
 *
 * @author Bryan Scott -- bryan@pajato.com
 */
class ScrollingTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

    @Test fun testScrolling() {
        val title = "video"
        val network = "network"

        val testTitle = "Stranger Things"
        val testNetwork = "Netflix"

        rule.activity.runOnUiThread {
            rule.activity.addVideo(Video(title + "1", network))
            rule.activity.addVideo(Video(title + "2", network))
            rule.activity.addVideo(Video(title + "3", network))
            rule.activity.addVideo(Video(title + "4", network))
            rule.activity.addVideo(Video(title + "5", network))
            rule.activity.addVideo(Video(title + "6", network))
            rule.activity.addVideo(Video(title + "7", network))
            rule.activity.addVideo(Video(title + "8", network))
            rule.activity.addVideo(Video(title + "9", network))
            rule.activity.addVideo(Video(title + "10", network))
            rule.activity.addVideo(Video(testTitle, testNetwork))
        }

        onView(withText(testTitle))
                .check(doesNotExist())

        onView(withId(R.id.listItems))
                .perform(swipeUp())
                .perform(swipeUp())

        onView(withText(testTitle))
                .check(matches(isDisplayed()))

        // Ensure after scrolling pressing a button affects the card view it is part of.
        checkViewVisibility(withText(testTitle), ViewMatchers.Visibility.VISIBLE)
        onView(allOf(withParent(hasSibling(withText(testTitle))), withId(R.id.dateButton)))
                .perform(click())
        respondToPermission(false)
        onView(allOf(hasSibling(withText(testTitle)), withId(R.id.dateText)))
                .check(matches(withText(not(""))))
    }
}