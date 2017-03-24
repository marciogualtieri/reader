package com.wire.reader.enums.ui;

public enum PreferencesActivityWidgets {
    SAVE_BUTTON(7), CANCEL_BUTTON(8), ENDPOINT_TEXT(9), OFFSET_NUMBER(10);

    private final int id;

    PreferencesActivityWidgets(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
