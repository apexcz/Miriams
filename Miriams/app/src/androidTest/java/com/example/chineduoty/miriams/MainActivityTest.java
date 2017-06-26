package com.example.chineduoty.miriams;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.chineduoty.miriams.model.Recipe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by chineduoty on 6/24/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public static final int SERVINGSCOUNT = 8;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void recipeItemShows() {
        onView(withRecyclerView(R.id.recipe_rv).atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
    }

    @Test
    public void activityHasCorrectIntent() {

        Intent recipeIntent = new Intent();
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Nutella Pie");
        recipe.setServings(SERVINGSCOUNT);

        recipeIntent.putExtra(Intent.EXTRA_TEXT, recipe);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, recipeIntent);

        Intents.init();
        // Set up result stubbing when an intent sent to "contacts" is seen.
        //intending(toPackage("com.example.chineduoty.miriams")).respondWith(result);

        intending(allOf(
                hasExtra(Intent.EXTRA_TEXT,recipe))
        ).respondWith(result);

        // Click item at position 0
        //onView(withRecyclerView(R.id.recipe_rv).atPosition(0)).perform(click());

        // Assert that data we set up above is shown.
        //onView(withId(R.id.recipe_serving_text)).check(matches(withText(""+SERVINGSCOUNT)));
    }


}
