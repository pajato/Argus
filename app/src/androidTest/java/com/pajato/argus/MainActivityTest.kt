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

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.NoActivityResumedException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.matcher.ViewMatchers.Visibility.INVISIBLE
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.app_bar_main.*
import org.hamcrest.Matcher
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Provide sufficient tests that the MainActivity class is 100% covered.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    /** Define the component under test using a JUnit rule. */
    @Rule @JvmField var activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    /** Ensure that the FAB button click handler code is invoked. */
    @Test fun testFabButton() {
        // Ensure that the fab button uses a + icon.
        checkViewVisibility(withId(R.id.fab), VISIBLE)
        onView(withId(R.id.fab)).perform(click())
        // TODO: ensure the search activity is invoked.
    }

    /** Ensure that the back button normally exits the app. */
    @Test fun testBackPress() {
        // Ensure that the nav drawer is closed and cause a back press
        // to happen.  And exception should be thrown.
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close())
        try {
            pressBack()
            fail("Did not get the expected exception!")
        } catch (exc: NoActivityResumedException) {
            // OK.  A PerformException is expected from the main (root) activity.
        } catch (exc: Exception) {
            fail("Wrong exception: $exc?.message")
        }
    }

    /** Ensure that a back press while the navigation drawer is open just closes the drawer. */
    @Test fun testBackPressUsingNavDrawer() {
        // Open the drawer and press the back button, which should work with no exception now.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()) // Open Drawer
        try {
            pressBack()
            checkViewVisibility(withId(R.id.nav_view), INVISIBLE)
        } catch (exc: Exception) {
            fail("Got an unexpected exception!")
        }
    }

    /** Test the navigation drawer menu items. */
    @Test fun testNavMenuItems() {
        for (title in listOf("Import", "Gallery", "Slideshow", "Tools", "Share", "Send"))
            clickOnNavItem(title)
    }

    /** Test the overflow menu Settings item. */
    @Test fun testOveflowMenuSettingsItem() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        checkViewVisibility(withText(R.string.action_settings), VISIBLE)
        onView(withText(R.string.action_settings)).perform(click())

        // Exercise the default case, the dummy test menu item.
        //activityRule.activity.clearFindViewByIdCache()
        val menu = activityRule.activity.toolbar.menu
        activityRule.activity.onOptionsItemSelected(menu.findItem(R.id.test_item))
    }

    /** Check that the initial display shows the main activity views. */
    @Test fun testInitialState() {
        checkViewVisibility(withId(R.id.nav_view), INVISIBLE)
        checkViewVisibility(withId(R.id.toolbar), VISIBLE)
        checkViewVisibility(withId(R.id.emptyListText), VISIBLE)
        checkViewVisibility(withId(R.id.emptyListText), VISIBLE)
        checkViewVisibility(withId(R.id.fab), VISIBLE)
    }

    // Private functions:

    /** Check that a view's (via the given matcher) has the given visibility. */
    private fun checkViewVisibility(viewMatcher: Matcher<View>, state: Visibility) {
        onView(viewMatcher).check(matches(withEffectiveVisibility(state)))
    }

    /** Check a hamburger menu item by looking for the given title and clicking on it. */
    private fun clickOnNavItem(title: String) {
        // Open the drawer,  and press the back button, which should work with no exception now.
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        checkViewVisibility(withId(R.id.nav_view), VISIBLE)
        onView(withText(title)).perform(click())
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close())
    }
}
