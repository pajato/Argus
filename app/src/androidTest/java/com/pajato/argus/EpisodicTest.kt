package com.pajato.argus

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.util.Log
import org.junit.Test

class EpisodicTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

    @Test fun testActivityResultTvShow() {
        val resultData = Intent()
        val title = "Stranger Things"
        resultData.putExtra(SearchActivity.TITLE_KEY, title)
        resultData.putExtra(SearchActivity.NETWORK_KEY, "Netflix")
        resultData.putExtra(SearchActivity.EPISODIC_KEY, true)

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        Intents.intending(IntentMatchers.toPackage("com.pajato.argus")).respondWith(result)
        onView(withId(R.id.fab))
                .perform(click())

        checkViewVisibility(ViewMatchers.withText(title), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.seasonText))
                .check(matches(withText("1")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("1")))
    }

    @Test fun testIncrements() {
        val title = "The Simpsons"
        val network = "Fox"
        rule.activity.runOnUiThread {
            rule.activity.addVideo(Episodic(title, network))
        }
        // Assert that the object comes in on Season 1 Episode 1
        checkViewVisibility(withText(title), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.seasonText))
                .check(matches(withText("1")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("1")))

        // Increment the episode. Ensure that the episode does change and the season does not.
        onView(withId(R.id.episodeLabel))
                .perform(click())
        onView(withId(R.id.seasonText))
                .check(matches(withText("1")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("2")))

        // Increment the season. Ensure that the season does change and the episode does not.
        onView(withId(R.id.seasonLabel))
                .perform(click())
        onView(withId(R.id.seasonText))
                .check(matches(withText("2")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("2")))
    }

    @Test fun testSets() {
        val title = "The Office"
        val network = "NBC"
        rule.activity.runOnUiThread {
            rule.activity.addVideo(Episodic(title, network))
        }
        checkViewVisibility(withText(title), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.seasonText))
                .check(matches(withText("1")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("1")))


        // Set the season with a long click and accept the change.
        onView(withId(R.id.seasonLabel))
                .perform(longClick())
        onView(withId(R.id.alertInput))
                .perform(replaceText("3"))
        onView(withId(android.R.id.button1))
                .perform(click())

        checkViewVisibility(withText(title), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.seasonText))
                .check(matches(withText("3")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("1")))

        // Set the season with a long click and discard the change.
        onView(withId(R.id.seasonLabel))
                .perform(longClick())
        onView(withId(R.id.alertInput))
                .perform(replaceText("1"))
        onView(withId(android.R.id.button2))
                .perform(click())
        checkViewVisibility(withText(title), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.seasonText))
                .check(matches(withText("3")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("1")))

        // Set the episode with a long click and accept the change.
        onView(withId(R.id.episodeLabel))
                .perform(longClick())
        onView(withId(R.id.alertInput))
                .perform(replaceText("3"))
        onView(withId(android.R.id.button1))
                .perform(click())
        checkViewVisibility(withText(title), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.seasonText))
                .check(matches(withText("3")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("3")))

        // Finally, ensure the season and episode data is persisted through a lifecycle.
        doLifeCycle()
        checkViewVisibility(withText(title), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.seasonText))
                .check(matches(withText("3")))
        onView(withId(R.id.episodeText))
                .check(matches(withText("3")))
    }
}