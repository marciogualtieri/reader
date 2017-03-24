package com.wire.reader.ui.test.idling.resources;

import android.support.test.espresso.IdlingResource;
import android.widget.ImageView;
import com.wire.reader.enums.ui.MainActivityWidgets;
import com.wire.reader.ui.MainActivity;

public class MainActivityIdlingResource implements IdlingResource {

    private MainActivity activity;
    private ResourceCallback callback;

    public MainActivityIdlingResource(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    private boolean isIdle() {
        return activity != null && callback != null && isFetchingMessages();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }

    private boolean isFetchingMessages() {
        return(!isProgressImageClickable());
    }

    private boolean isProgressImageClickable() {
        ImageView progress = (ImageView) activity.findViewById(MainActivityWidgets.PROGRESS_IMAGE.id());
        return(progress != null && progress.isShown());
    }
}