package com.kalynx.snagtest.modelobserver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class NotifyController implements Notify {
    private Map<NotifyKey<?>, Set<Consumer<?>>> listeners = new HashMap<>();
    @Override
    public <T> void addListener(NotifyKey<T> key, Consumer<T> consumer) {
        if(!listeners.containsKey(key)) {
            listeners.put(key, new HashSet<>());
        }
        listeners.get(key).add(consumer);
    }

    @Override
    public <T> void removeListener(NotifyKey<T> key, Consumer<T> consumer) {
        Set<?> consumers = listeners.get(key);
        if(consumers != null) {
            consumers.remove(consumer);
        }
    }

    public <T> void notify(NotifyKey<T> key, T valueChange) {
        // Is protected by the listener interfaces and key. The NotifyKey ensures safety with the exception
        // for when key equivallency being bruteforced.
        Set<T> listenerList = (Set<T>) listeners.get(key);
        if(listenerList != null) {
            listenerList.stream().forEach(consumer -> ((Consumer<T>) consumer).accept(valueChange));
        }
    }
}
