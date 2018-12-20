package com.github.chisui.translate;

import java.lang.reflect.Type;

/**
 * Represents a Translation key.
 * Unless you really know what you are you should not extend this interface directly. Either extends
 * {@link EnumTranslationKey} or use {@link ClassTranslationKey}.
 *
 * A translation key represents the key component of a translation. The type parameter {@code A} represents the type of
 * arguments that this key expects. {@code A} does not have a value that is associated with a key but rather a so called
 * shadow type parameter that is only used to indicate to the compiler that certain constraints hold. This mechanism is
 * used on {@link TranslationFunction} to ensure that certain methods can only be called if {@code A} fulfills certain
 * constraints. For example {@link TranslationFunction#apply(TranslationKey)} can only be called for keys where
 * {@code A} is {@link Void}.
 *
 * The <a href="https://github.com/chisui/translate/tree/master/translate-verify">translate-verify</a> artifact provides
 * ways to verify in tests that implementations of {@link TranslationKey} follow this contract and that messages
 * corresponding to keys actually do have the a shape that excepts arguments of type {@code A}.
 *
 * To obtain a {@link TranslationKey} you should either create them using {@link #of(Translatable)} or
 * {@link #of(Class)} if you want to create keys for {@link Translatable} subclasses or create an {@link Enum} that
 * implements {@link EnumTranslationKey} with the appropriate bound value for {@code A}. When implementing
 * {@link EnumTranslationKey} {@code SELF} <strong>has</strong> to be the implementing {@link Enum} itself.
 * You may create intermediate interfaces that extend {@link EnumTranslationKey} or {@link TranslationKey} but the
 * actual classes that implement these interfaces have to be {@link Enum}s and {@code SELF} type parameter
 * <strong>has</strong> to be the concrete type of the implementing enum. That means the {@code SELF} type parameter
 * has to e passed through.
 *
 * <pre>
 *     interface MyKey<SELF extends MyKey<SELF>> extends EnumTranslationKey<SELF, String>
 * </pre>
 *
 * For further information consult the <a href="https://github.com/chisui/translate/wiki/Tutorial">Tutorial</a>.
 *
 * @param <SELF> self referential type. This always has to be the exact type that implements the interface.
 * @param <A> the type of argument this translation key expects. If it can be multiple use An array. If the key doesn't
 *           expect any argument use {@link Void}.
 * @see EnumTranslationKey
 * @see ClassTranslationKey
 * @see TranslationFunction
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
public interface TranslationKey<SELF extends TranslationKey<SELF, A>, A> {

    /**
     * Returns the actual type of {@code A}. This does not have to be a {@link Class} since {@code A} may be a
     * parametrized type like {@code List<String>}. Although for collection types you should use array types anyways.
     *
     * @return the argument type
     */
    Type argType();

    /**
     * Turns this key into a String that can be used for lookup.
     * Both {@link EnumTranslationKey} and {@link ClassTranslationKey} use
     * {@link DefaultKeyToString#defaultToKeyString(TranslationKey)} to create their string representations.
     *
     * <strong>You shouldn't implement this method yourself. If you are, you are probably not using the {@code translate}
     * API correctly.</strong>
     *
     * @return the key string representation of this key
     * @see DefaultKeyToString
     */
    String toKeyString();

    /**
     * Creates a {@link TranslationKey} for a given {@link Translatable} instance.
     *
     * @param c the {@link Translatable} instance
     * @param <C> the type of the {@link Translatable} instance
     * @return a {@link TranslationKey} representing the type of the {@link Translatable} instance
     */
    @SuppressWarnings({
            "unchecked", // c.getClass() always returns a Class<C>, but java's type system sucks bigtime.
    })
    static <C extends Translatable> ClassTranslationKey<C> of(C c) {
        return of((Class<C>) c.getClass());
    }

    /**
     * Creates a {@link TranslationKey} for a given {@link Translatable} type.
     *
     * @param c the class of the {@link Translatable} type
     * @param <C> the {@link Translatable} type
     * @return a {@link TranslationKey} representing the {@link Translatable} type
     */
    static <C extends Translatable> ClassTranslationKey<C> of(Class<C> cls) {
        return new ClassTranslationKey<>(cls);
    }
}
