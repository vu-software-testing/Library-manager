package com.example.librarymanager.testing.espresso;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.librarymanager.R;
import com.example.librarymanager.books.BooksActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class BooksActivityTest {

    @Rule
    public ActivityTestRule<BooksActivity> mActivityRule =
            new ActivityTestRule<>(BooksActivity.class);

    @Test
    public void shouldGoToAddBookFragment() {
        onView(withId(R.id.gv_grid)).check(matches(isDisplayed()));

        onView(withId(R.id.fab)).perform(click());

        onView(withText("Add book")).check(matches(isDisplayed()));

        onView(withContentDescription("Navigate up")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withText("Log out")).check(matches(isDisplayed()));
    }
}
