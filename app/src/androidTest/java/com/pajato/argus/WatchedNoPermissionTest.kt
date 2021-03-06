package com.pajato.argus

import android.Manifest
import android.Manifest.permission.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import org.junit.Test
import java.text.DateFormat
import java.util.*
import android.support.test.uiautomator.UiObjectNotFoundException
import android.os.Build
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.util.Log
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not


/**
 * Tests the three permutations for location permissions. 1. Being prompted and refusing permission
 * 2. Being prompted and accepting the permission. 3. Having already accepted the permission and
 * not requiring the prompt again.
 *
 * @author Bryan Scott -- bryan@pajato.com
 */
class WatchedNoPermissionTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

    @Test
    fun testPermissions() {
        // Exercise the onRequestPermissionsResult edge cases.
        val emptyResults = IntArray(0)
        val requestCode = MainActivity.LOCATION_REQUEST_CODE
        val results = IntArray(1)
        results[0] = -1
        rule.activity.onRequestPermissionsResult(-1, arrayOf(), emptyResults)
        rule.activity.onRequestPermissionsResult(requestCode, arrayOf(), emptyResults)
        rule.activity.onRequestPermissionsResult(requestCode, arrayOf(), results)
        results[0] = PERMISSION_GRANTED
        rule.activity.onRequestPermissionsResult(requestCode, arrayOf(), results)

        // Add a video to the UI to setup our tests.
        val luther = Video("Luther", "HBO Go")
        val simpsons = Video ("The Simpsons", "Fox")
        rule.activity.runOnUiThread {
            rule.activity.addVideo(luther)
            rule.activity.addVideo(simpsons)
        }
        // Establish some viewmatchers.
        val dateButtonMatcherLuther = allOf(withId(R.id.dateButton), withParent(hasSibling(withText(luther.title))))
        val locTextMatcherLuther = allOf(withId(R.id.locationText), hasSibling(withText(luther.title)))
        val dateTextMatcherLuther = allOf(withId(R.id.dateText), hasSibling(withText(luther.title)))

        // Set the date and prompt the app to request location access, which we initially refuse.
        checkViewVisibility(dateButtonMatcherLuther, ViewMatchers.Visibility.VISIBLE)
        onView(dateButtonMatcherLuther)
                .perform(click())
        respondToPermission(false)

        // Ensure blocking location still causes the date to change, but does not change the location.
        val date = DateFormat.getDateInstance().format(Date())
        checkViewVisibility(dateTextMatcherLuther, ViewMatchers.Visibility.VISIBLE)
        onView(dateTextMatcherLuther)
                .check(matches(withText(date)))
        checkViewVisibility(locTextMatcherLuther, ViewMatchers.Visibility.VISIBLE)
        onView(locTextMatcherLuther)
                .check(matches(withText("")))

        // Now prompt the app to request location access again, and allow it.
        checkViewVisibility(dateButtonMatcherLuther, ViewMatchers.Visibility.VISIBLE)
        onView(dateButtonMatcherLuther)
                .perform(click())
        respondToPermission(true)

        // Ensure that a location has been entered.
        checkViewVisibility(locTextMatcherLuther, ViewMatchers.Visibility.VISIBLE)
        onView(locTextMatcherLuther)
                .check(matches(not(withText(""))))

        // Do a lifecycle to ensure that the location text is persisted in the database.
        doLifeCycle()
        checkViewVisibility(locTextMatcherLuther, ViewMatchers.Visibility.VISIBLE)
        onView(locTextMatcherLuther)
                .check(matches(not(withText(""))))

        // Finally do one last update to the location to cover the use case where location permission has already been granted.
        val dateButtonMatcherSimpsons = allOf(withId(R.id.dateButton), withParent(hasSibling(withText(simpsons.title))))
        val locTextMatcherSimpsons = allOf(withId(R.id.locationText), hasSibling(withText(simpsons.title)))
        val dateTextMatcherSimpsons = allOf(withId(R.id.dateText), hasSibling(withText(simpsons.title)))

        onView(locTextMatcherSimpsons)
                .check(matches(withText("")))
        onView(dateTextMatcherSimpsons)
                .check(matches(withText("")))
        onView(dateButtonMatcherSimpsons)
                .perform(click())
        onView(locTextMatcherSimpsons)
                .check(matches(not(withText(""))))
        onView(dateTextMatcherSimpsons)
                .check(matches(withText(date)))
    }

}
