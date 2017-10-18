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
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
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
        onView(withId(R.id.searchName)).perform(replaceText(text))
        onView(withText(text)).check(matches(isDisplayed()))
        onView(withId(R.id.save_button)).check(matches(not(isEnabled())))
        onView(withId(R.id.searchName)).perform(replaceText(text))
        onView(withId(R.id.save_button)).check(matches(not(isEnabled())))

        val network = "HBO Go"
        onView(withId(R.id.network)).perform(replaceText(network))
        onView(withId(R.id.save_button)).check(matches(isEnabled()))
    }

    @Test fun testNetworkAutoComplete() {
        // Select the HBO network, ensure that the save button is now enabled and click on it.
        val hint = "h"
        val network = "HBO Go"
        onView(withId(R.id.searchName)).perform(replaceText(""))
        onView(withId(R.id.network))
                .perform(click())
                .perform(replaceText(hint))
        onData(equalTo(network)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.network)).check(matches(withText(network)))
    }

}
