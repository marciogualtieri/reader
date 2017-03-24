package com.wire.reader.enums.ui;

public enum MainActivityWidgets {
    READ_BUTTON(1), PREFS_BUTTON(2), PURGE_BUTTON(3), QUIT_BUTTON(4), PROGRESS_IMAGE(5), MESSAGE_LIST(6);

    private final int id;

    MainActivityWidgets(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}