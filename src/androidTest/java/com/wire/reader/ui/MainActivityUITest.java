package com.wire.reader.ui;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.wire.reader.entitities.Message;
import com.wire.reader.enums.ui.MainActivityWidgets;
import com.wire.reader.ui.test.helpers.UITestHelper;
import com.wire.reader.ui.test.idling.resources.MainActivityIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.wire.reader.ui.test.helpers.UITestHelper.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUITest {

    private MainActivityIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void runBeforeTestMethod() throws Exception {
        UITestHelper.setupPreferences();
        registerMainActivityIdelResource();
    }

    @After
    public void runAfterTestMethod() throws Exception {
        Espresso.unregisterIdlingResources(idlingResource);
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
    public void whenReadClick_thenGetMessages() throws Exception {
        purgeAllMessages();
        onView(withId(MainActivityWidgets.READ_BUTTON.id())).perform(click());
        MainActivity activity = mainActivityRule.getActivity();
        List<Message> messages = extractMessagesFromListView(activity, MainActivityWidgets.MESSAGE_LIST.id());
        assertListsEqual(AllTestMessages, messages);
    }

    private void registerMainActivityIdelResource() {
        MainActivity activity = mainActivityRule.getActivity();
        idlingResource = new MainActivityIdlingResource(activity);
        Espresso.registerIdlingResources(idlingResource);
    }
}