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
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.support.test.espresso.matcher.ViewMatchers.withText
import kotlinx.android.synthetic.main.app_bar_main.*
import org.junit.Test

/**
 * Provide sufficient tests that the MainActivity class is 100% covered.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
class OptionsMenuTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

    /** Test the overflow menu "Settings" item. */
    @Test fun testOverflowMenuSettingsItem() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        checkViewVisibility(withText(R.string.action_settings), VISIBLE)
        onView(withText(R.string.action_settings)).perform(click())

        // Exercise the default case, the dummy test menu item.
        val mainActivity = rule.activity
        val menu = mainActivity.toolbar.menu
        mainActivity.onOptionsItemSelected(menu.findItem(R.id.test_item))
    }
}
