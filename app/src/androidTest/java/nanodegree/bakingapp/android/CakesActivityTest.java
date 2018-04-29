package nanodegree.bakingapp.android;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nanodegree.bakingapp.android.activities.CakesActivity;
import nanodegree.bakingapp.android.activities.IngredientsActivity;
import nanodegree.bakingapp.android.fragments.CakesFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class CakesActivityTest {

    private static final String FIRS_CAKE = "Nutella Pie";

    @Rule
    public IntentsTestRule<CakesActivity> mCakeActivityTestRule =
            new IntentsTestRule<>(CakesActivity.class, false, true);

    private CountingIdlingResource mIdlingResource;

    @Before
    public void registerIdleResourceAndInitCakeFragment() {
        mCakeActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.cakesFragmentContainer, CakesFragment.getFragmentInstance())
                .commit();
        mIdlingResource = CakesActivity.mCountingIdleResource;
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void scrollOnFirstPositionOfRecyclerView_DisplayNutellaPie() {
        onView(withId(R.id.mainCakesRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.mainCakesRecyclerView)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(FIRS_CAKE)).check(matches(isDisplayed()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void clickOnRecyclerViewItem_OpensIngredientActivity() {
        onView(withId(R.id.mainCakesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(hasComponent(IngredientsActivity.class.getName())));
        onView(withText(R.string.ingredient_label)).check(matches(isDisplayed()));
        onView(withText(R.string.steps_label)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdleResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

}
