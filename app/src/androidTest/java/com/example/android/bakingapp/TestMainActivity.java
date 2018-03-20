package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by namit on 3/20/2018.
 */
@RunWith(AndroidJUnit4.class)
public class TestMainActivity {
    @Before
    public void init(){
        mainActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
@Test
public void RecipeListDisplayed(){
    onView(withId(R.id.idRecyclerViewVerticaList)).check(matches(isDisplayed()));
}
    @Test
    public void clickOnRecyclerView () {
    //    RecipeFragment fragment = new RecipeFragment();
      //  mainActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        onView(withId(R.id.idRecyclerViewVerticaList)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        onView(withId(R.id.title_container)).check(matches(isDisplayed()));

    }
}

