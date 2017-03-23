package com.wire.reader.ui;

import android.app.Instrumentation;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.wire.reader.enums.ui.MainActivityWidgets;
import com.wire.reader.ui.test.helpers.UITestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.wire.reader.ui.test.helpers.UITestHelper.assertActivityDoesNotExists;
import static com.wire.reader.ui.test.helpers.UITestHelper.assertActivityExists;
import static com.wire.reader.ui.test.helpers.UITestHelper.createActivityMonitor;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUITest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void runAfterTestMethod() {
        UITestHelper.setupPreferences();
    }

    @Test
    public void whenPreferencesClick_thenOpenPreferences() {
        Instrumentation.ActivityMonitor prefsActivityMonitor = createActivityMonitor(PreferencesActivity.class);
        onView(withId(MainActivityWidgets.PREFS_BUTTON.id())).perform(click());
        assertActivityExists(prefsActivityMonitor);
    }

    @Test
    public void whenQuitClick_thenFinishMainActivity() {
        Instrumentation.ActivityMonitor mainActivityMonitor = createActivityMonitor(MainActivity.class);
        assertActivityDoesNotExists(mainActivityMonitor);
    }

    @Test
    public void whenReadClick_thenGetMessages() {
        onView(withId(MainActivityWidgets.READ_BUTTON.id()))
                .perform(click())
                .perform(swipeDown());
    }

}