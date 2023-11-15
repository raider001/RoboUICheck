package com.kalynx.snagtest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RobotKeywordName {
    String value() default "";
}
