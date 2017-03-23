package com.wire.reader.enums.ui;

public enum PreferencesActivityWidgets {
    SAVE_BUTTON(5), CANCEL_BUTTON(6), ENDPOINT_TEXT(7), OFFSET_NUMBER(8);

    private final int id;

    PreferencesActivityWidgets(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
