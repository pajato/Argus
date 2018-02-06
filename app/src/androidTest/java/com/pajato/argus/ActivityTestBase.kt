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
import android.content.Intent
import android.os.Build
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObjectNotFoundException
import android.support.test.uiautomator.UiSelector
import android.util.Log
import android.view.View
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Provide an abstract base class for all test classes.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
@RunWith(AndroidJUnit4::class) abstract class ActivityTestBase<T : Activity>(theClass: Class<T>) {

    /** Define the component under test using a JUnit rule. */
    @Rule @JvmField val rule: ActivityTestRule<T> = IntentsTestRule(theClass)

    @Before fun setDatabaseName() {
        // TODO: Find a way to ensure this gets accomplished before the *first* test activity is loaded.
        DatabaseReaderHelper.setDatabaseName("ArgusTest.db")
    }

    @After fun clearDatabase() {
        // Cleanup after a test is run.
        deleteAll(rule.activity.applicationContext)
    }

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

    /** ... */
    fun <T : Activity> nextOpenActivityIs(theClass: Class<T>) {
        intended(IntentMatchers.hasComponent(theClass.name))
    }

    /** Provide an extension on the Activity class to run code on the UI thread. */
    fun Activity.runOnUiThread(f: () -> Unit) {
        runOnUiThread { f() }
    }

    fun Activity.getIDName(id: Int): String {
        return this.resources.getResourceEntryName(id)
    }

    protected fun doLifeCycle(intent: Intent? = null) {
        rule.activity.finish()
        Intents.release()

        rule.launchActivity(intent)
    }

    /** A shorthand method that will accept or deny the permission popup. */
    protected fun respondToPermission(allow: Boolean) {
        if (Build.VERSION.SDK_INT >= 23) {
            val s = if (allow) "ALLOW" else "DENY"
            val allowPermissions = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).findObject(UiSelector().text(s))
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click()
                } catch (e: UiObjectNotFoundException) {
                    Log.v(this::class.java.canonicalName, "Click Machine Broke")
                }
            }
        }
    }
}
