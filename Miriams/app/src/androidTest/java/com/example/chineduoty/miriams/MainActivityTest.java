package com.example.chineduoty.miriams;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

/**
 * Created by chineduoty on 6/24/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest  {

    public static final String SERVINGSCOUNT = "8";
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public static void clickRecipeItems_OpensRecipeDetailActivity(){
        onData(anything()).inAdapterView(withId(R.id.recipe_rv)).atPosition(1).perform(click());

        //Checks that the details activity shows the servings of the recipe
        onView(withId(R.id.recipe_serving_text)).check(matches(withText(SERVINGSCOUNT)));
    }
}
