package com.example.skyqgorecruitmenttest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.skyqgorecruitmenttest.data.model.Data
import com.example.skyqgorecruitmenttest.data.model.MovieRepo
import com.example.skyqgorecruitmenttest.ui.activity.MainActivity
import com.example.skyqgorecruitmenttest.viewModel.MainViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isActivityInView(){

        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun testTitleTextDisplayed() {
        onView(withId(R.id.btnRetry))
            .check(matches(withText(R.string.retry)))
    }



}