package com.example.librarymanager.testing.espresso;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.librarymanager.R;
import com.example.librarymanager.launch.LaunchActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LaunchActivityTest {

    @Rule
    public ActivityTestRule<LaunchActivity> mActivityRule = new ActivityTestRule<>(
            LaunchActivity.class);

    @Test
    public void shouldBeAbleToLaunchSplashScreen() {
        onView(ViewMatchers.withId(R.id.vu_logo)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldGoToLoginFragmentIfClicked() {
        onView(withId(R.id.vu_logo)).perform(click());

        onView(withId(R.id.input_vunetid)).check(matches(isDisplayed()));
        onView(withId(R.id.input_passwd)).check(matches(isDisplayed()));
    }
}
