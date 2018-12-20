package com.github.chisui.translate;

/**
 * Represents a way to turn a {@link TranslationKey} into a string that can be used to lookup messages.
 *
 * The default implementation of this interface is {@link DefaultKeyToString}.
 *
 * This interface is equivalent to {@code Function<TranslationKey<?, ?>, String>} but it also checks that the type
 * signature of the provided key is consistent. That is the self referential type is actual a reference to the type
 * itself.
 *
 * @see DefaultKeyToString
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
@FunctionalInterface
public interface KeyToString {

    /**
     * Apply this {@link KeyToString} function to the provided {@link TranslationKey}
     *
     * @param key to apply this function to
     * @param <SELF> self referential type of the key. Has to be the same as the actual key type.
     * @param <A> the Argument type the key accepts
     * @return the string representation of the key
     */
    <SELF extends TranslationKey<SELF, A>, A> String toKeyString(TranslationKey<SELF, A> key);

}
