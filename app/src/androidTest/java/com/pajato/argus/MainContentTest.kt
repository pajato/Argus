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

import android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import kotlinx.android.synthetic.main.app_bar_main.*
import org.junit.Test

/**
 * Test the main content configurations.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
class MainContentTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

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
