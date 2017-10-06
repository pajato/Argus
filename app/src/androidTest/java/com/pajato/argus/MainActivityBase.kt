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

import android.app.Activity
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.view.View
import org.hamcrest.Matcher

/**
 * Provide sufficient tests that the MainActivity class is 100% covered.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
//@RunWith(AndroidJUnit4::class)
open class MainActivityBase() {

    /** Check that a view's (via the given matcher) has the given visibility. */
    fun checkViewVisibility(viewMatcher: Matcher<View>, state: Visibility) {
        onView(viewMatcher).check(matches(withEffectiveVisibility(state)))
    }

    /** Check a hamburger menu item by looking for the given title and clicking on it. */
    fun clickOnNavItem(title: String) {
        // Open the drawer,  and press the back button, which should work with no exception now.
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        checkViewVisibility(withId(R.id.nav_view), VISIBLE)
        onView(withText(title)).perform(click())
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close())
    }

    /** Provide an extension on the Activity class to run code on the UI thread. */
    fun Activity.runOnUiThread(f: () -> Unit) {
        runOnUiThread { f() }
    }
}
