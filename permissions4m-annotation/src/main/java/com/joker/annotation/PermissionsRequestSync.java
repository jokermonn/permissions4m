package com.joker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by joker on 2017/7/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PermissionsRequestSync {
    int[] value();

    String[] permission();
}
