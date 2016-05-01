package com.example.librarymanager.testing.espresso;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.librarymanager.R;
import com.example.librarymanager.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void shouldShowVunetidErrorIfVunetidEmpty() {
        onView(withId(R.id.btn_login)).perform(click());
        String errorText = mActivityRule.getActivity().getResources().getString(R.string.vunetid_error_message);
        onView(withId(R.id.input_vunetid)).check(matches(Matchers.withError(errorText)));
    }

    @Test
    public void shouldShowPasswordErrorIfPasswordEmpty() {
        onView(withId(R.id.input_vunetid)).perform(typeText("admin"));
        onView(withId(R.id.btn_login)).perform(click());
        String errorText = mActivityRule.getActivity().getResources().getString(R.string.password_error_message);
        onView(withId(R.id.input_passwd)).check(matches(Matchers.withError(errorText)));
    }

    @Test
    public void shouldShowErrorIfVunetidAndPasswordIsIncorrect() {

        onView(withId(R.id.input_vunetid)).perform(typeText("admin"));
        onView(withId(R.id.input_passwd)).perform(typeText("bla"));
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.input_vunetid)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldGoToMainActivityIfCredentialsAreCorrect() {
        onView(withId(R.id.input_vunetid)).perform(typeText("admin"));
        onView(withId(R.id.input_passwd)).perform(typeText("admin"));
        onView(withId(R.id.btn_login)).perform(click());

        onView(withId(R.id.gv_grid)).check(matches(isDisplayed()));

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText("Log out")).perform(click());

        onView(withId(R.id.input_vunetid)).check(matches(isDisplayed()));
        onView(withId(R.id.input_passwd)).check(matches(isDisplayed()));
    }
}
