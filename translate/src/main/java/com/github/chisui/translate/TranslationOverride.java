package com.github.chisui.translate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Optional;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface TranslationOverride {
    String value() default "";
    String baseKey() default "";
    String prefix() default "";
}
