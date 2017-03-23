package com.wire.reader.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
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
import static junit.framework.Assert.assertNotNull;

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
    public void whenReadClick_thenGetMessages() {
        onView(withId(MainActivityWidgets.READ_BUTTON.id()))
                .perform(click())
                .perform(swipeDown());
    }

    @Test
    public void whenQuitClick_thenFinishMainActivity() {
        onView(withId(MainActivityWidgets.QUIT_BUTTON.id())).perform(click());
    }

    private Instrumentation.ActivityMonitor createActivityMonitor(Class activityClass) {
        return(InstrumentationRegistry.getInstrumentation()
                .addMonitor(activityClass.getName(), null, false));
    }

    private void assertActivityExists(Instrumentation.ActivityMonitor activityMonitor) {
        Activity prefsActivity = InstrumentationRegistry
                .getInstrumentation()
                .waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(prefsActivity);
    }

}