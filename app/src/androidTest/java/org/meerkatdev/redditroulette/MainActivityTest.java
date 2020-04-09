package org.meerkatdev.redditroulette;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.meerkatdev.redditroulette.data.Subreddit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openMainActivity_ChecksFirstTwoRecipesAreDisplayed() {
        Subreddit[] subreddits = new Subreddit[]{};
        onView(withText(subreddits[0].name)).check(matches(isDisplayed()));
        onView(withText(subreddits[1].name)).check(matches(isDisplayed()));
    }

    @Test
    public void openMainActivity_ChecksRecyclerViewElementsLengthIsCorrect() {
        //Recipe[] recipes = activityRule.getActivity().getRecipesFromJson(activityRule.getActivity());
        onView(withId(R.id.rv_recipes)).check(new RecyclerViewItemCountAssertion(recipes.length));
    }

    @Test
    public void clickRecipeRecyclerView_OpensRecipeStepsActivity() {
        Recipe[] recipes = activityRule.getActivity().getRecipesFromJson(activityRule.getActivity());
        Recipe whichRecipe = recipes[0];
        int recipeStep = 3;
        onView(withText(whichRecipe.name)).perform(click()); // click on the first element of the list
        onView(withId(R.id.rv_recipe_steps)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipe_steps)).check(matches(hasDescendant(withText(whichRecipe.steps[recipeStep].shortDescription))));
    }

    // https://stackoverflow.com/questions/36399787/how-to-count-recyclerview-items-with-espresso
    class RecyclerViewItemCountAssertion implements ViewAssertion {

        private final Matcher<Integer> matcher;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.matcher = is(expectedCount);
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), matcher);
        }

    }
}
