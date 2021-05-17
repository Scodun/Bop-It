package com.se2.bopit.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MiniGameType {
    String DEFAULT_NAME = "default";
    boolean DEFAULT_ENABLED = true;

    String name() default DEFAULT_NAME;

    boolean enableByDefault() default DEFAULT_ENABLED;
}
