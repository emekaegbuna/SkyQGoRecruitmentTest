package com.example.skyqgorecruitmenttest

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Test
    fun isActivityInView(){

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }


    // Text
    @Test
    fun testTitleTextDisplayed() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btnRetry))
            .check(matches(withText(R.string.retry)))
    }

}