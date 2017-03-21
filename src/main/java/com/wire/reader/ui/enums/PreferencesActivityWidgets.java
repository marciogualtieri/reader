package com.wire.reader.ui.enums;

public enum PreferencesActivityWidgets {
    SAVE_BUTTON(4), CANCEL_BUTTON(5), ENDPOINT_TEXT(6), OFFSET_TEXT(11);

    private final int id;

    PreferencesActivityWidgets(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
