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

/**
 * Created by chineduoty on 6/24/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    public static final String SERVINGS = "Servings";
    public static final String STEP1 = "Step 1";

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityRule =
            new ActivityTestRule(RecipeDetailActivity.class);

    @Test
    public void ensureRecipeDetailShows(){

        onView((withId(R.id.servings_label))).check(matches(withText(SERVINGS)));
    }

    @Test
    public static void clickStepItems_OpensStepDetailFragment(){
        onData(anything()).inAdapterView(withId(R.id.steps_rv)).atPosition(1).perform(click());

        //Checks that the details activity shows the servings of the recipe
        onView(withId(R.id.step_count_text)).check(matches(withText(STEP1)));
    }

}
