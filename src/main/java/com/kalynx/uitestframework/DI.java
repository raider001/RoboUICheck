package com.kalynx.uitestframework;

import com.kalynx.lwdi.DependencyInjector;

public class DI {
    private static DependencyInjector dependencyInjector = new DependencyInjector();

    private DI() {

    }
    public synchronized static final DependencyInjector getInstance() {
        if(dependencyInjector == null) {
            dependencyInjector = new DependencyInjector();
        }
        return dependencyInjector;
    }

    public static void reset() {
        dependencyInjector = null;
    }

}
