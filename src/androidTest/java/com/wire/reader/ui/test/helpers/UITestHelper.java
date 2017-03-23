package com.wire.reader.ui.test.helpers;

import android.support.test.InstrumentationRegistry;
import android.content.SharedPreferences;
import com.wire.reader.constants.ReaderPreferences;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class UITestHelper {
    public static final String TEST_ENDPOINT =
            "https://rawgit.com/marciogualtieri/reader/master/src/test/resources/reader";

    public static final int TEST_OFFSET = 0;

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

    private static SharedPreferences getSharedPreferences() {
        return(InstrumentationRegistry.getInstrumentation()
                .getTargetContext()
                .getSharedPreferences("READER_PREFERENCES", 0));
    }
}
