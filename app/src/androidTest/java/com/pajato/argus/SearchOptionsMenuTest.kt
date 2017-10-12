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

import kotlinx.android.synthetic.main.activity_search.*
import org.junit.Test

/**
 * Provide sufficient tests that the MainActivity class is 100% covered.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
class SearchOptionsMenuTest : ActivityTestBase<SearchActivity>(SearchActivity::class.java) {

    /** Test the overflow menu test item. */
    @Test fun testOveflowMenuSettingsItem() {
        // Exercise the default case, the dummy test menu item.
        val activity = rule.activity
        val menu = activity.searchToolbar.menu
        activity.onOptionsItemSelected(menu.findItem(R.id.test_item))
    }
}
