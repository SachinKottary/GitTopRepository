package com.sachin.gogit;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<GoGitActivity> mActivityRule =
            new ActivityTestRule<>(GoGitActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.sachin.gogit", appContext.getPackageName());
    }

    @Test
    public void checkIfFragmentIsVisible() {
        onView(withId(R.id.main)).check(matches(isDisplayed()));
    }

    @Test
    public void checkNoNetworkViewDisplayedFirstTime() {
        disableWifi();
        onView(withId(R.id.network_error_image)).check(matches(isDisplayed()));
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfProgressBarShown() {
        sleepThread(1000);
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfDataIsLoaded() {
        onView(withId(R.id.git_repo_recycler_view))//TODO need to manually change first item, or make API call and get 1st item
                .check(matches(atPosition(0, hasDescendant(withText("ageron")))));
    }

    @Test
    public void checkIfDetailViewExpanded() {
        onView(withId(R.id.git_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
    }

    @Test
    public void checkIfDetailViewCollapsedAfterExpand() {
        onView(withId(R.id.git_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
        onView(withId(R.id.git_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), withEffectiveVisibility(ViewMatchers.Visibility.GONE))))));
    }

    @Test
    public void checkIfScrollPositionStateMaintained() {
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.avatar_image), isDisplayed())))));
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.avatar_image), isDisplayed())))));
    }

    @Test
    public void checkIfScrollPositionStateMaintainedInScrollState() {
        onView(withId(R.id.git_repo_recycler_view))
                .perform(scrollToPosition(24));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(17, hasDescendant(allOf(withId(R.id.avatar_image), isDisplayed())))));
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(17, hasDescendant(allOf(withId(R.id.avatar_image), isDisplayed())))));
    }

    @Test
    public void checkIfClickedItemSavesStateWhenScreenRotated() {
        onView(withId(R.id.git_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
    }

    @Test
//With scroll depends on the screen device display when testing, test case might be differ for small display devices
    public void checkIfClickedItemSavesStateWhenScreenRotatedWithScroll() {
        onView(withId(R.id.git_repo_recycler_view))
                .perform(scrollToPosition(24));
        onView(withId(R.id.git_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(17, click()));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(17, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(17, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
    }

    @Test
    public void checkStateMaintainedInMultipleActivityRecreation() {
        onView(withId(R.id.git_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(0, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
    }

    @Test
    public void checkStateMaintainedInMultipleActivityRecreationWithScroll() {
        onView(withId(R.id.git_repo_recycler_view))
                .perform(scrollToPosition(24));
        onView(withId(R.id.git_repo_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(17, click()));
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(17, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(17, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.git_repo_recycler_view))
                .check(matches(withViewAtPosition(17, hasDescendant(allOf(withId(R.id.description_details), isDisplayed())))));
    }

    private void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void disableWifi() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        WifiManager wifi = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);
    }

    public static Matcher<View> withViewAtPosition(final int position, final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

}
