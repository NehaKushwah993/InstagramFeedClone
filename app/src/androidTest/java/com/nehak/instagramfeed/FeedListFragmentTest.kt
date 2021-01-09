package com.nehak.instagramfeed

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FeedListFragmentTest {


    lateinit var scenario : FragmentScenario<FeedListFragment>;

    @Before
    fun launch(){
        //launch the fragment before each test
        val scenario = launchFragmentInContainer<FeedListFragment>()
    }

    @Test
    fun is_RootView_Visible_shouldBe_True() {
        onView(withId(R.id.root_view_feed_fragment)).check(matches(isDisplayed()))

    }

    @Test
    fun is_RecyclerView_Visible_shouldBe_True() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun is_ListItem_Visible_shouldBe_True() {
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withId(R.id.image_view))))
    }



}