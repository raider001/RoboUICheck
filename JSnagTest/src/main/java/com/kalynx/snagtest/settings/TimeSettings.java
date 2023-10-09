package com.kalynx.snagtest.settings;

import com.kalynx.snagtest.modelobserver.NotifyController;
import com.kalynx.snagtest.modelobserver.NotifyKey;

import java.util.function.Consumer;

public class TimeSettings {
    private static final NotifyKey<Integer> TYPE_DELAY_KEY = new NotifyKey<>();
    private final NotifyController notifyController = new NotifyController();
    private int typeDelay = 20;

    public int getTypeDelay() {
        return typeDelay;
    }

    /**
     * The typing delay between keystrokes.
     *
     * @param typeDelay The delay in milliseconds.
     */
    public void setTypeDelay(int typeDelay) {
        this.typeDelay = typeDelay;
        notifyController.notify(TYPE_DELAY_KEY, this.typeDelay);
    }

    public void addTypeDelayListener(Consumer<Integer> listener) {
        notifyController.addListener(TYPE_DELAY_KEY, listener);
    }

    public void removeTypeDelayListener(Consumer<Integer> listener) {
        notifyController.removeListener(TYPE_DELAY_KEY, listener);
    }
}
