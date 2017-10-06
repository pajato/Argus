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

import android.support.test.espresso.matcher.ViewMatchers.Visibility.INVISIBLE
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

/**
 * Provide sufficient tests that the MainActivity class is 100% covered.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
class MainActivityTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

    /** Check that the initial display shows the main activity views. */
    @Test fun testInitialState() {
        checkViewVisibility(withId(R.id.nav_view), INVISIBLE)
        checkViewVisibility(withId(R.id.toolbar), VISIBLE)
        checkViewVisibility(withId(R.id.fab), VISIBLE)
    }
}
