package com.se2.bopit.domain.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declare required sensor for the mini game.
 * MiniGameRegistry will accept the mini game only if sensor is available.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireSensor {
    /**
     * Sensor type constant from class Sensor, e.g. Sensor#TYPE_LIGHT
     */
    int value();
}
