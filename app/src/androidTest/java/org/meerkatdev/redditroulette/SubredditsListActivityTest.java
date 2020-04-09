package org.meerkatdev.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.SmallTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@SmallTest
public class RecipeStepsListActivityTest {

    @Rule
    public IntentsTestRule<RecipeStepsListActivity> mActivityRule = new IntentsTestRule<>(
            RecipeStepsListActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        Intent intent = new Intent();
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, intent));
    }

    @Test
    public void clickRecipeStepRecyclerView_Renders() throws InterruptedException {

        onView(withId(R.id.steps_list_fragment)).check(matches(hasDescendant(withId(R.id.rv_recipe_steps))));
    }



    // NOTE: one or the other will fail depending on what device is used for the testing

    @Test
    public void beingOnPhone_AssertStepContentDoesNotExist() {
        onView(withId(R.id.steps_list_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.step_content_fragment)).check(doesNotExist());
    }

    // NOTE: how to exclude/include tests based on device used?

    @Test
    @Ignore
    public void beingOnTablet_AssertStepContentIsDisplayed() {
        onView(withId(R.id.steps_list_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.step_content_fragment)).check(matches(isDisplayed()));
    }
}
