package com.github.chisui.translate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static java.util.Collections.singletonList;

/**
 * The default way to turn translation keys into string keys.
 *
 * Constructs the key string using {@code "<CLASS>.<NAME>"} where
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
 * @see TranslationOverride
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
public final class DefaultKeyToString implements KeyToString {

    private static final DefaultKeyToString INSTANCE = new DefaultKeyToString();

    private DefaultKeyToString() {}

    /**
     * Returns the singleton instance of {@link DefaultKeyToString}.
     *
     * @return the singleton instance
     */
    public static DefaultKeyToString instance() {
        return INSTANCE;
    }

    @Override
    public <Self extends TranslationKey<Self, Arg>, Arg> String toKeyString(
            final TranslationKey<Self, Arg> key) {
        return defaultToKeyString(key);
    }

    /**
     * The default way to turn a {@link TranslationKey} into a string key.
     *
     * Constructs the key string using {@code "<CLASS>.<NAME>"} where
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
     * @param key to turn into a string key
     * @param <Self> self referential type argument of the key
     * @param <Arg> argument type of the key
     * @return the string representation of the key.
     * @see TranslationOverride
     * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
     */
    public static <Self extends TranslationKey<Self, Arg>, Arg> String defaultToKeyString(
            final TranslationKey<Self, Arg> key) {
        String baseStr = toKeyStringBase(key);
        if (key instanceof Enum && key instanceof EnumTranslationKey) {
            Field enumConstantField = enumConstantField((Enum<?>) key);
            String enumStr = Optional.ofNullable(enumConstantField.getDeclaredAnnotation(TranslationOverride.class))
                    .map(TranslationOverride::value)
                    .orElseGet(() -> ((Enum) key).name().toLowerCase());
            return baseStr + "." + enumStr;
        } else {
            return baseStr;
        }
    }

    private static Field enumConstantField(
            final Enum<?> constant) {
        try {
            return constant.getClass()
                    .getField(constant.name());
        } catch (NoSuchFieldException e) {
            // Can only occur if reflection is disabled, an extra enum instance was created through reflection or Unsafe
            // or maybe if the byte code was edited?
            throw new IllegalStateException("expected enum constant but got " + constant + " of type " + constant.getClass(), e);
        }
    }

    private static <A, SELF extends TranslationKey<SELF, A>> String toKeyStringBase(
            final TranslationKey<SELF, A> key) {
        Class<?> cls = cls(key);
        return translationOverride(cls)
                .map(override -> toKeyStringBase(cls, override))
                .orElseGet(cls::getCanonicalName);
    }

    private static Optional<TranslationOverride> translationOverride(
            final Class<?> cls) {
        Queue<Class<?>> clss = new LinkedList<>(singletonList(cls));
        while (!clss.isEmpty()) {
            Class<?> c = clss.poll();
            if (c == Object.class || c == null) {
                break;
            }
            TranslationOverride override = c.getDeclaredAnnotation(TranslationOverride.class);
            if (override != null) {
                return Optional.of(override);
            } else {
                Arrays.stream(c.getInterfaces())
                        .forEach(clss::offer);
                clss.offer(c.getSuperclass());
            }
        }
        return Optional.empty();
    }

    private static String toKeyStringBase(
            final Class<?> cls,
            final TranslationOverride override) {
        return Optional.of(override.value())
                .filter(DefaultKeyToString::isNotBlank)
                .map(Optional::of)
                .orElseGet(() -> Optional.of(override.prefix())
                        .map(prefix -> prefix + "." + cls.getSimpleName()))
                .orElseGet(cls::getCanonicalName);
    }

    private static boolean isNotBlank(String s) {
        return !s.trim().isEmpty();
    }

    private static <Self extends TranslationKey<Self, Arg>, Arg> Class<?> cls(
            final TranslationKey<Self, Arg> key) {
        if (key instanceof ClassTranslationKey) {
            return ((ClassTranslationKey<?>) key).cls();
        } else if (key instanceof EnumTranslationKey && key instanceof Enum) {
            return key.getClass();
        } else {
            throw new IllegalArgumentException("can not determine translation key class of " + key);
        }
    }

    @Override
    public String toString() {
        return "DefaultKeyToString";
    }
}
