package com.example.chineduoty.miriams;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by chineduoty on 6/24/17.
 */

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {

    @Rule
    public ActivityTestRule<StepDetailActivity> mActivityRule =
            new ActivityTestRule(StepDetailActivity.class);


    @Test
    public void ensureStepNavigationButtonShows(){
        onView(withId(R.id.previous_btn)).perform(click()).check(matches(isDisplayed()));
    }
}
