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

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Provide sufficient tests that the MainActivity class is 100% covered.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
@RunWith(AndroidJUnit4::class) class FabButtonTest : MainActivityBase() {

    /** Define the component under test using a JUnit rule. */
    @Rule @JvmField val rule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    /** Ensure that the FAB button click handler code is invoked. */
    @Test fun testFabButton() {
        // Ensure that the fab button uses a + icon.
        checkViewVisibility(withId(R.id.fab), VISIBLE)
        onView(withId(R.id.fab)).perform(click())
        // TODO: ensure the search activity is invoked.
    }
}
