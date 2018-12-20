package com.github.chisui.translate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static com.github.chisui.translate.DefaultKeyToString.defaultToKeyString;
import static java.util.Arrays.asList;

/**
 * Represents a Translation key.
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
 * When implementing {@link EnumTranslationKey} {@code SELF} <strong>has</strong> to be the implementing {@link Enum}
 * itself. You may create intermediate interfaces that extend {@link EnumTranslationKey} or {@link TranslationKey} but
 * the actual classes that implement these interfaces have to be {@link Enum}s and {@code SELF} type parameter
 * <strong>has</strong> to be the concrete type of the implementing enum. That means the {@code SELF} type parameter
 * has to e passed through.
 *
 * <pre>
 *     interface MyKey<SELF extends MyKey<SELF>> extends EnumTranslationKey<SELF, String>
 * </pre>
 *
 * For further information consult the <a href="https://github.com/chisui/translate/wiki/Tutorial">Tutorial</a>.
 *
 * @param <SELF> self referential type. This always has to be the exact enum type that implements the interface.
 * @param <A> the type of argument this translation key expects. If it can be multiple use An array. If the key doesn't
 *           expect any argument use {@link Void}.
 * @see TranslationKey
 * @see ClassTranslationKey
 * @see TranslationFunction
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
public interface EnumTranslationKey<
        SELF extends Enum<SELF> & EnumTranslationKey<SELF, A>,
        A> extends TranslationKey<SELF, A> {

    /**
     * Returns the actual type of {@code A}. This does not have to be a {@link Class} since {@code A} may be a
     * parametrized type like {@code List<String>}. Although for collection types you should use array types anyways.
     * This default implementation retrieves the type by using reflection to introspect the Enum type itself.
     *
     * @return the argument type
     */
    default Type argType() {
        // TODO create cache like the one guava Eventbus uses to resolve @Subscribe annotations.
        Queue<Type> clss = new LinkedList<>(asList(getClass().getGenericInterfaces()));
        while (!clss.isEmpty()) {
            Type t = clss.poll();
            if (t instanceof ParameterizedType) {
                ParameterizedType p = (ParameterizedType) t;
                if (EnumTranslationKey.class.equals(p.getRawType())) {
                    return p.getActualTypeArguments()[1];
                } else {
                    Arrays.stream(((Class<?>) p.getRawType()).getGenericInterfaces())
                            .forEach(clss::offer);
                }
            } else {
                break;
            }
        }
        throw new IllegalStateException(getClass() + " implements EnumTranslationKey wrong.");
    }


    /**
     * Turns this key into a String that can be used for lookup.
     * This default implementation uses {@link DefaultKeyToString#defaultToKeyString(TranslationKey)} to create the
     * string representations.
     *
     * <strong>You shouldn't implement this method yourself. If you are, you are probably not using the {@code translate}
     * API correctly.</strong>
     *
     * @return the key string representation of this key
     * @see DefaultKeyToString
     */
    default String toKeyString() {
        return defaultToKeyString(this);
    }
}
