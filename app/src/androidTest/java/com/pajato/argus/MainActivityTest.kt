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

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.toPackage
import android.support.test.espresso.matcher.ViewMatchers.Visibility.*
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.inputmethod.InputMethodManager
import org.junit.Assert.*
import org.junit.Test

/**
 * Provide sufficient test cases that the MainActivity class is at or near 100% coverage.
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

    @Test fun testDismissKeyboard() {
        // Load up a video and give focus to the EditText
        rule.activity.runOnUiThread {
            rule.activity.addVideo(Video("Luther", "HBO Go"))
        }
        checkViewVisibility(withId(R.id.listItems), VISIBLE)
        checkViewVisibility(withId(R.id.titleText), VISIBLE)
        onView(withId(R.id.titleText)).perform(click())

        // Ensure that the input is accepted once we click, and stops accepting once we click on the CardView
        val imm: InputMethodManager = rule.activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assertTrue("Input is not accepting text when selecting EditText titleText", imm.isAcceptingText)
        assertEquals("titleText does not have focus", rule.activity.getIDName(R.id.titleText),
                rule.activity.getIDName(rule.activity.currentFocus.id))
        onView(withId(R.id.card_view)).perform(click())
        assertFalse("Input is still accepting text after attempting to change focus", imm.isAcceptingText)
        assertEquals("card_view does not have focus", rule.activity.getIDName(R.id.card_view),
                rule.activity.getIDName(rule.activity.currentFocus.id))

        // Ensure that input is accepted for network text, and stops accepting once we click on the base RecyclerView
        onView(withId(R.id.networkText)).perform(click())
        assertTrue("Input is not accepting text when selecting EditText networkText", imm.isAcceptingText)
        assertEquals("networkText does not have focus", rule.activity.getIDName(R.id.networkText),
                rule.activity.getIDName(rule.activity.currentFocus.id))
        onView(withId(R.id.card_view)).perform(click())
        assertFalse("Input is still accepting text after attempting to change focus", imm.isAcceptingText)
        assertEquals("listItems does not have focus", rule.activity.getIDName(R.id.card_view),
                rule.activity.getIDName(rule.activity.currentFocus.id))
    }

    @Test fun testActivityResultNullData() {
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(toPackage("com.pajato.argus")).respondWith(result)
        onView(withId(R.id.fab)).perform(click())
        checkViewVisibility(withId(R.id.emptyListIcon), VISIBLE)
    }

    @Test fun testActivityResultCanceled() {
        val result = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)
        intending(toPackage("com.pajato.argus")).respondWith(result)
        onView(withId(R.id.fab)).perform(click())
        checkViewVisibility(withId(R.id.emptyListIcon), VISIBLE)
    }
}
