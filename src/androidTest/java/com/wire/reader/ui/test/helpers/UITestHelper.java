package com.wire.reader.ui.test.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.widget.ListView;
import com.wire.reader.constants.ReaderPreferences;
import com.wire.reader.entitities.Message;
import com.wire.reader.enums.ui.MainActivityWidgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.*;

public class UITestHelper {
    public static final String TEST_ENDPOINT =
            "https://rawgit.com/marciogualtieri/reader/master/src/test/resources/reader";

    public static final int TEST_OFFSET = 0;

    private static final int ACTIVITY_MONITOR_TIMEOUT_IN_MILISECS = 5000;

    private static final Message george1 = new Message("george1",  1463372338305L,  "You’ve got to apologize.", 0);
    private static final Message jerry1 = new Message("jerry1", 1463372338485L, "Why?", 0);
    private static final Message george2 = new Message("george2",  1463372338665L,  "Because it’s the mature and adult thing to do.",  0);
    private static final Message jerry2 = new Message("jerry2",  1463372338845L,  "How does that affect me?",  0);

    public static final List<Message> TestMessagesEndpoint0 = Arrays.asList(george1, jerry1, george2, jerry2);

    private static final Message customer1 = new Message("customer1",  1463372339025L,  "Now, what is the difference between the GT and the GTS?",  1);
    private static final Message larry1 = new Message("larry1",  1463372339205L,  "OK, the GTS is \"guaranteed tremendous safety\".",  1);
    private static final Message customer2 = new Message("customer2",  1463372339385L,  "So, without the \"S\", it's just \"guaranteed tremendous\"?",  1);

    public static final List<Message> TestMessagesEndpoint1 = Arrays.asList(customer1, larry1, customer2);

    private static final Message robot1 = new Message("robot1",  1463372339565L,  "What's my purpose?",  2);
    private static final Message rick1 = new Message("rick1",  1463372339745L,  "You pass butter.",  2);
    private static final Message robot2 = new Message("robot2",  1463372339925L,  "Oh, my god...",  2);
    private static final Message rick2 = new Message("rick2",  1463372340105L,  "Yeah. Welcome to the club, pal.",  2);
    private static final Message rick3 = new Message("rick3",  1463372340285L,  "https://rawgit.com/marciogualtieri/reader/master/src/test/resources/reader/rickandmorty.png",  2);
    private static final Message rick4 = new Message("rick4",  1463372340465L,  "https://this.is.a/broken/link/image/morty.png",  2);

    public static final List<Message> TestMessagesEndpoint2 = Arrays.asList(robot1, rick1, robot2, rick2, rick3, rick4);

    public static List<Message> AllTestMessages = new ArrayList<>(TestMessagesEndpoint0);

    static {
        AllTestMessages.addAll(TestMessagesEndpoint1);
        AllTestMessages.addAll(TestMessagesEndpoint2);
    }

    public static void setupPreferences() {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ReaderPreferences.ENDPOINT, UITestHelper.TEST_ENDPOINT);
        editor.putInt(ReaderPreferences.OFFSET, UITestHelper.TEST_OFFSET);
        editor.apply();
    }

    public static String getEndpointPreference() {
        SharedPreferences preferences = getSharedPreferences();
        return(preferences.getString(ReaderPreferences.ENDPOINT, null));
    }

    public static int getOffsetPreference() {
        SharedPreferences preferences = getSharedPreferences();
        return(preferences.getInt(ReaderPreferences.OFFSET, 0));
    }

    public static void inputText(int textWidgetId, String text) {
        onView(withId(textWidgetId))
                .perform(clearText())
                .perform(typeText(text))
                .perform(closeSoftKeyboard());
    }

    public static void clickButton(int buttonWidgetId) {
        onView(withId(buttonWidgetId))
                .perform(click());
    }

    public static void purgeAllMessages() {
        onView(withId(MainActivityWidgets.PURGE_BUTTON.id())).perform(click());
        onView(withText("OK")).perform(click());
    }

    public static Instrumentation.ActivityMonitor createActivityMonitor(Class activityClass) {
        return(InstrumentationRegistry.getInstrumentation()
                .addMonitor(activityClass.getName(), null, false));
    }

    public static List<Message> extractMessagesFromListView(Activity activity, int listViewId) {
        ListView listView = (ListView) activity.findViewById(listViewId);
        List<Message> messages = extractMessagesFromListView(listView);
        return(messages);
    }

    public static void assertActivityExists(Instrumentation.ActivityMonitor activityMonitor) {
        Activity activity = getActivity(activityMonitor);
        assertNotNull(activity);
    }

    public static void assertActivityDoesNotExists(Instrumentation.ActivityMonitor activityMonitor) {
        Activity activity = getActivity(activityMonitor);
        assertNull(activity);
    }

    public static <T> void assertListsEqual(List<T> first, List<T> second) {
        assertTrue(first.containsAll(second) && second.containsAll(first));
    }

    private static List<Message> extractMessagesFromListView(ListView listView) {
        List<Message> messages = new ArrayList<>();
        for(int i = 0; i < listView.getCount(); i++) {
            Message message = (Message) listView.getItemAtPosition(i);
            messages.add(message);
        }
        return(messages);
    }

    private static Activity getActivity(Instrumentation.ActivityMonitor activityMonitor) {
        return(InstrumentationRegistry
                .getInstrumentation()
                .waitForMonitorWithTimeout(activityMonitor,
                        ACTIVITY_MONITOR_TIMEOUT_IN_MILISECS));
    }

    private static SharedPreferences getSharedPreferences() {
        return(InstrumentationRegistry.getInstrumentation()
                .getTargetContext()
                .getSharedPreferences("READER_PREFERENCES", 0));
    }
}
