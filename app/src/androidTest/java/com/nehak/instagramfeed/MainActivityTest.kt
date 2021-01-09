package com.nehak.instagramfeed

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
public class MainActivityTest {

    @Rule
    @JvmField
    public var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun FeedListFragment_isVisible_True() {

        onView(withId(R.id.feed_list_fragment)).check(matches(isDisplayed()));
    }
}