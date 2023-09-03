package com.kalynx.snagtest.threading;

interface ConditionalRunnable<T> extends Runnable {

    @Override
    default void run() {
        if(checkCondition(runAction())) {
            onTrue();
        }
    }

    T runAction();

    /**
     * Fires a condition based on the data. When true is returned, it cancels
     * @param data
     * @return
     */
    default boolean checkCondition(T data) {
        return false;
    }

    void onTrue();
}
