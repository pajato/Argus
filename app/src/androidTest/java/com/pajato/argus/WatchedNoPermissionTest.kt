package com.pajato.argus

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
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.util.Log
import org.hamcrest.CoreMatchers.not


class WatchedNoPermissionTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {

    @Test
    fun testPermissions() {

        rule.activity.runOnUiThread {
            val video = Video("Luther", "HBO Go")
            rule.activity.addVideo(video)
        }

        checkViewVisibility(withId(R.id.dateButton), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.dateButton))
                .perform(click())

        allowPermission(false)

        val date = DateFormat.getDateInstance().format(Date())
        checkViewVisibility(withId(R.id.dateText), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.dateText))
                .check(matches(withText(date)))
        checkViewVisibility(withId(R.id.locationText), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.locationText))
                .check(matches(withText("")))



        checkViewVisibility(withId(R.id.dateButton), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.dateButton))
                .perform(click())

        allowPermission(true)
        checkViewVisibility(withId(R.id.locationText), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.locationText))
                .check(matches(not(withText(""))))

        doLifeCycle()
        checkViewVisibility(withId(R.id.locationText), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.locationText))
                .check(matches(not(withText(""))))

    }

    private fun allowPermission(allow: Boolean) {
        if (Build.VERSION.SDK_INT >= 23) {
            val s = if (allow) {
                "ALLOW"
            } else {
                "DENY"
            }
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