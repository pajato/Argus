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
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.NoActivityResumedException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.matcher.ViewMatchers.Visibility.*
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
@RunWith(AndroidJUnit4::class) class MainContentTest : MainActivityBase() {

    /** Define the component under test using a JUnit rule. */
    @Rule @JvmField val rule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    /** Check that the empty video list content is correct. */
    @Test fun testEmptyList() {
        // Disable the non-empty video list content and enable the empty list video content.
        val mainActivity = rule.activity
        mainActivity.runOnUiThread {
            mainActivity.emptyListFrame.visibility = View.VISIBLE
            mainActivity.nonEmptyListFrame.visibility = View.GONE
        }
        checkViewVisibility(withId(R.id.emptyListIcon), VISIBLE)
        checkViewVisibility(withId(R.id.emptyListText), VISIBLE)
        checkViewVisibility(withId(R.id.listItems), GONE)
    }

    /** Check that the non-empty video list content is correct. */
    @Test fun testNonEmptyList() {
        // Disable the empty video list content and enable the non-empty video list content.
        val mainActivity = rule.activity
        mainActivity.runOnUiThread {
            mainActivity.emptyListFrame.visibility = View.GONE
            mainActivity.nonEmptyListFrame.visibility = View.VISIBLE
        }
        checkViewVisibility(withId(R.id.emptyListIcon), GONE)
        checkViewVisibility(withId(R.id.emptyListText), GONE)
        checkViewVisibility(withId(R.id.listItems), VISIBLE)
    }
}
