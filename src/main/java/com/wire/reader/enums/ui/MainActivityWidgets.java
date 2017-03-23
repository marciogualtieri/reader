package com.wire.reader.enums.ui;

public enum MainActivityWidgets {
    READ_BUTTON(1), PREFS_BUTTON(2), QUIT_BUTTON(3), MESSAGE_LIST(4);

    private final int id;

    MainActivityWidgets(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}