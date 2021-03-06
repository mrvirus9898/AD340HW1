package com.example.athena.ad340hw1;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.athena.ad340hw1.SupportPackKt.isEmpty;
import static com.example.athena.ad340hw1.SupportPackKt.loadString;
import static com.example.athena.ad340hw1.SupportPackKt.saveString;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.athena.ad340hw1", appContext.getPackageName());
    }

    //testChangeText verifys both the rendering and function of the textbox and button
    //and I didnt need to make a new class or object to do it. THE POWER OF KOTLIN
    @Test
    public void testChangeText_sameActivity() {

        onView(withId(R.id.textInputRobot))
                .check(matches(isDisplayed()));

        onView(withId(R.id.robot_button))
                .check(matches(isDisplayed()));

        //You really didn't think that I wouldn't find this little gem did you?
        String original = new String(loadString(mActivityRule.getActivity().prefs, "favRobot"));

        // Type text and then press the button.
       onView(withId(R.id.textInputRobot))
                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
        onView(withId(R.id.robot_button)).perform(click());

        System.out.println(loadString(mActivityRule.getActivity().prefs, "favRobot") + " " + (original + mStringToBetyped));

        assertTrue(loadString(mActivityRule.getActivity().prefs, "favRobot").equals(original + mStringToBetyped));

        saveString(mActivityRule.getActivity().prefs,"favRobot",original);

        // Check that the text was changed.

    }
}
