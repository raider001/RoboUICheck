package com.kalynx.uitestframework;

import com.kalynx.lwdi.DependencyInjector;

public class DI {
    private static DependencyInjector di = new DependencyInjector();

    public synchronized static final DependencyInjector getInstance() {
        if(di == null) {
            di = new DependencyInjector();
        }
        return di;
    }

}
