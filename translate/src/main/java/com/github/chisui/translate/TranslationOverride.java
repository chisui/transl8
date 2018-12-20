package com.github.chisui.translate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Provides a way to override the behavior of {@link DefaultKeyToString} on a enum, to enum basis.
 *
 * By default {@link DefaultKeyToString} constructs the key string using {@code "<CLASS>.<NAME>"} where
 * {@code <CLASS>} is the {@link Class#getCanonicalName() canonical name} of the enums class and {@code <NAME>} is the
 * {@link Enum#name() name} of the enum constant. For {@link Translatable} classes only the canonical name is used.
 *
 * {@link TranslationOverride} can be used to modify this behavior. If you use this annotation on a type you can use
 * {@link #value() value} to override the entire {@code <CLASS>} component of the key string. with
 * {@link #prefix() prefix} you can override the package part of the class name. For example:
 *
 * <pre>
 * @TranslationOverride(prefix = "myPrefix")
 * class MyTranslatable implements Translatable {
 *     ...
 * </pre>
 *
 * will always result in the key string {@code "myPrefix.MyTranslatable"} regardless of what package
 * {@code MyTranslatable} actually resides in.
 *
 * {@code prefix} will also work on superclasses and super interfaces This makes it possible to create a common
 * interface for {@link Translatable} and {@link EnumTranslationKey} in your project that manages centrally manages the
 * translation key prefix.
 *
 * When {@link TranslationOverride} is used on an enum constant only {@link #value() value} has any effect. It replaces
 * the {@code <NAME>} part of the key string.
 *
 * @see DefaultKeyToString
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface TranslationOverride {

    /**
     * When using {@link DefaultKeyToString} {@link #value()} can be used to override the class name part of a key
     * string if used on a {@link EnumTranslationKey} {@link Enum} or {@link Translatable}. If used on an enum constant
     * it overrides the name part of the key string
     *
     * @return the override value
     * @see DefaultKeyToString
     */
    String value() default "";

    /**
     * When using {@link DefaultKeyToString} {@link #prefix()} can be used to override the package name part of a key
     * string. The class name is still appended to the prefix.
     *
     *  {@code prefix} will also work on superclasses and super interfaces This makes it possible to create a common
     * interface for {@link Translatable} and {@link EnumTranslationKey} in your project that manages centrally manages
     * the translation key prefix.
     *
     * @return the override prefix
     * @see DefaultKeyToString
     */
    String prefix() default "";
}
