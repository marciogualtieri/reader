package com.wire.reader.ui;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.wire.reader.enums.ui.PreferencesActivityWidgets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.wire.reader.ui.test.helpers.UITestHelper.*;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PreferencesActivityUITest {

    private final String inputEndpoint = "http://some/endpoint/url";
    private final int inputOffset = 9;

    @Rule
    public ActivityTestRule<PreferencesActivity> prefsActivityRule =
            new ActivityTestRule<>(PreferencesActivity.class);

    @Before
    public void runAfterTestMethod() {
        setupPreferences();
    }

    @Test
    public void whenChangeEndpointAndSave_thenChangeIsSaved() {
        inputText(PreferencesActivityWidgets.ENDPOINT_TEXT.id(),
                inputEndpoint);
        clickButton(PreferencesActivityWidgets.SAVE_BUTTON.id());
        assertEquals(inputEndpoint, getEndpointPreference());
    }

    @Test
    public void whenChangeEndpointAndCancel_thenChangeIsNotSaved() {
        inputText(PreferencesActivityWidgets.ENDPOINT_TEXT.id(),
                inputEndpoint);
        clickButton(PreferencesActivityWidgets.CANCEL_BUTTON.id());
        assertEquals(getEndpointPreference(), TEST_ENDPOINT);
    }

    @Test
    public void whenChangeOffsetAndSave_thenOffsetIsSaved() {
        inputText(PreferencesActivityWidgets.OFFSET_NUMBER.id(),
                Integer.toString(inputOffset));
        clickButton(PreferencesActivityWidgets.SAVE_BUTTON.id());
        assertEquals(inputOffset, getOffsetPreference());
    }

    @Test
    public void whenChangeOffsetAndCancel_thenOffsetIsNotSaved() {
        inputText(PreferencesActivityWidgets.OFFSET_NUMBER.id(),
                Integer.toString(inputOffset));
        clickButton(PreferencesActivityWidgets.CANCEL_BUTTON.id());
        assertEquals(getOffsetPreference(), TEST_OFFSET);
    }

}
