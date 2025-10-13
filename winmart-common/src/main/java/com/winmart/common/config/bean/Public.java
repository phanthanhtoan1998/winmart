package com.winmart.common.config.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark endpoints that do not require authentication. Can be used at the class
 * (Controller) or method level.
 */
@Target({ElementType.METHOD, ElementType.TYPE}) // Can be applied to both method and class
@Retention(RetentionPolicy.RUNTIME) // Needs to be retained at runtime for Spring Security to read
public @interface Public {}
