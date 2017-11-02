/*
* Copyright (C) 2017 Pajato Technologies LLC.
*
* This file is part of Pajato GameChat.

* GameChat is free software: you can redistribute it and/or modify it under the terms of the GNU
* General Public License as published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.

* GameChat is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* General Public License for more details.

* You should have received a copy of the GNU General Public License along with GameChat.  If not,
* see http://www.gnu.org/licenses
*/

package com.pajato.argus

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import kotlinx.android.synthetic.main.activity_search.*
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Provide sufficient tests that the MainActivity class is 100% covered.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
class SearchActivityTest : ActivityTestBase<SearchActivity>(SearchActivity::class.java) {

    /** Check that the initial display shows the activity views. */
    @Test fun testInitialState() {
        // The toolbar, search name and network name edit widgets should be visible.
        checkViewVisibility(withId(R.id.searchToolbar), VISIBLE)
        checkViewVisibility(withId(R.id.searchName), VISIBLE)
        checkViewVisibility(withId(R.id.searchName), VISIBLE)

        // The options menu item should be visible but disabled by default.
        checkViewVisibility(withId(R.id.save_button), VISIBLE)
        onView(withId(R.id.save_button)).check(matches(not(isEnabled())))
    }

    @Test fun testSaveEnabling() {
        // Edit a value into the name edit text box and ensure the save button is still not enabled.
        val text = "Some video text"
        val network = "HBO Go"

        // When the title name is entered but the network is not we should not be able to save
        onView(withId(R.id.searchName)).perform(replaceText(text))
        onView(withId(R.id.save_button)).check(matches(not(isEnabled())))
        onView(withId(R.id.network)).perform(pressImeActionButton()).check(matches(isDisplayed()))

        // When the title is not entered but the network is entered, we should still not be able to save
        onView(withId(R.id.searchName)).perform(replaceText(""))
        onView(withId(R.id.network)).perform(replaceText(network))
        onView(withId(R.id.save_button)).check(matches(not(isEnabled())))
        onView(withId(R.id.network)).perform(pressImeActionButton()).check(matches(isDisplayed()))

        // When both have text, we should be able to save.
        onView(withId(R.id.searchName)).perform(replaceText(text))
        onView(withId(R.id.network)).perform(replaceText(network))
        onView(withId(R.id.save_button)).check(matches(isEnabled()))
        onView(withId(R.id.network)).perform(pressImeActionButton())
        assertTrue(rule.activity.isFinishing)
    }

    @Test fun testAllNetworkAutoCompletes() {
        // Go through all the hints by letter and check that the adapter behaves appropriately
        val map = getHintToNetworkCountMap()
        for (key in map.keys) {
            val value = map.getValue(key)
            testAutoCompleteForHint(key, value)
        }
    }

    private fun testAutoCompleteForHint(hint: String, networkCount: Int) {
        // Focus on the network AutoCompleteTextView and add the hint text to it
        val network = getNetworkForHint(hint)
        onView(withId(R.id.searchName)).perform(replaceText(""))
        onView(withId(R.id.network))
                .perform(click())
                .perform(replaceText(hint))

        // Check that the correct number of options are present for a hint, and if a network is
        // provided that it's present in the adapter.
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(network))).inRoot(RootMatchers.isPlatformPopup())
        assertEquals("Incorrect Adapter Data for " + hint, networkCount, rule.activity.network.adapter.count)
        if (network != "") {
            onData(equalTo(network)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
            onView(withId(R.id.network)).check(matches(withText(network)))
        }
    }

    private fun getNetworkForHint(key: String): String {
        val hint = key.toLowerCase()
        // The master list that converts from a hint to the full network names.
        when (hint) {
            "ab" -> return "ABC"
            "am" -> return "Amazon"
            "b" -> return "BritBox"
            "cb" -> return "CBS"
            "ci" -> return "Cinemax"
            "cr" -> return "Crunchyroll"
            "cw", "th" -> return "The CW"
            "f" -> return "Fox"
            "goo", "pl" -> return "Google Play"
            "hbo g", "go" -> return "HBO Go"
            "hbo n", "no" -> return "HBO Now"
            "hu" -> return "Hulu"
            "nb" -> return "NBC"
            "ne" -> return "Netflix"
            "pb" -> return "PBS"
            "sl" -> return "Sling"
            "st" -> return "Starz"
            "sh" -> return "Showtime"
            "tb" -> return "TBS"
            "y", "r" -> return "YouTube Red"
            else -> return ""
        }
    }

    // A map of all the hints to the number of network autocompletion options that they should generate.
    private fun getHintToNetworkCountMap(): Map<String, Int> {
        return mapOf("a" to 2, "ab" to 1, "am" to 1,
                "b" to 1,
                "c" to 4, "cb" to 1, "ci" to 1, "cr" to 1, "cw" to 1,
                "d" to 0,
                "e" to 0,
                "f" to 1,
                "g" to 2, "go" to 2, "goo" to 1,
                "h" to 3, "hb" to 2, "hbo g" to 1, "hbo n" to 1, "hu" to 1,
                "i" to 0,
                "j" to 0,
                "k" to 0,
                "l" to 0,
                "m" to 0,
                "n" to 3, "nb" to 1, "ne" to 1, "no" to 1,
                "o" to 0,
                "p" to 2, "pb" to 1, "pl" to 1,
                "q" to 0,
                "r" to 1,
                "s" to 3, "sh" to 1, "sl" to 1, "st" to 1,
                "t" to 2, "tb" to 1, "th" to 1,
                "u" to 0,
                "v" to 0,
                "w" to 0,
                "x" to 0,
                "y" to 1,
                "z" to 0)
    }
}
