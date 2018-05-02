package nanodegree.bakingapp.android;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nanodegree.bakingapp.android.activities.CakesActivity;
import nanodegree.bakingapp.android.activities.IngredientsActivity;
import nanodegree.bakingapp.android.fragments.CakesFragment;
import nanodegree.bakingapp.android.fragments.IngredientsFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class IngredientsActivityTest {
    private static final String DUMMY_TEXT_TO_SHARE = "this is dummy text for testing purposes";

    @Rule
    public ActivityTestRule<CakesActivity> mCakeActivityTestRule =
            new ActivityTestRule<>(CakesActivity.class, false, true);

    @Rule
    public IntentsTestRule<IngredientsActivity> mIngredientsActivityTestRule =
            new IntentsTestRule<>(IngredientsActivity.class);
    private CountingIdlingResource mIdlingResource;

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    public static Matcher<Intent> chooser(Matcher<Intent> matcher) {
        return allOf(hasAction(Intent.ACTION_CHOOSER), hasExtra(is(Intent.EXTRA_INTENT), matcher));
    }

    @Before
    public void registerIdleResourceAndStubExternalIntents() {
        mCakeActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.cakesFragmentContainer, CakesFragment.getFragmentInstance())
                .commit();
        mIdlingResource = CakesActivity.mCountingIdleResource;
        IdlingRegistry.getInstance().register(mIdlingResource);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    private void clickOnItemInCakesRecyclerView_OpensIngredientsActivity() {
        onView(withId(R.id.mainCakesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        mIngredientsActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ingredientsFragmentContainer, IngredientsFragment.getFragmentInstance())
                .commitAllowingStateLoss();
    }

    @Test
    public void fabMenuGroup_HasShareAndAddFABs() {
        clickOnItemInCakesRecyclerView_OpensIngredientsActivity();
        onView(allOf(withParent(withId(R.id.menuFab)), withClassName(endsWith("ImageView")), isDisplayed()));
        onView(nthChildOf(withId(R.id.menuFab), 0)).check(matches(withId(R.id.shareFab)));
        onView(nthChildOf(withId(R.id.menuFab), 1)).check(matches(withId(R.id.addToWidgetFab)));
    }

    /**
     * before running this test
     * (TODO) in order to make this test pass
     * please go to IngredientsActivity Line : 144
     * and change the parameter with DUMMY_TEXT_TO_SHARE
     */
    @Test
    public void clickOnShareFab_SharesIngredients() {
        clickOnItemInCakesRecyclerView_OpensIngredientsActivity();
        onView(allOf(withParent(withId(R.id.menuFab)), withClassName(endsWith("ImageView")), isDisplayed()))
                .perform(click());
        onView(withId(R.id.shareFab)).perform(click());

        intended(chooser(allOf(
                hasAction(Intent.ACTION_SEND),
                hasExtra(Intent.EXTRA_TEXT, DUMMY_TEXT_TO_SHARE)
        )));
    }

    @After
    public void unregisterIdleResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
