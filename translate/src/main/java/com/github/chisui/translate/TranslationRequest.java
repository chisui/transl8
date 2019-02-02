package com.github.chisui.translate;

import java.util.Objects;

import static java.util.Arrays.copyOf;
import static java.util.Objects.requireNonNull;

/**
 * Arg combination of a {@link TranslationKey} and an instance of it's associated argument type.
 * This is meant as a way to pass around all information needed for a translation in a single data structure.
 *
 * @param <Key> Type of key
 * @param <Arg> Type of argument
 * @see <a href="https://github.com/chisui/translate/tree/master/translate-verify">Tutorial</a>
 */
public final class TranslationRequest<Key extends TranslationKey<Key, ? super Arg>, Arg> {

    private final Key key;
    private final Arg arg;

    private TranslationRequest(
            final Key key,
            final Arg arg) {
        this.key = requireNonNull(key, "key");
        this.arg = key.argType() == Void.class
                ? arg // null is only allowed as argument if the argument type is Void
                : requireNonNull(arg, "arg");
    }

    /**
     * Create a {@link TranslationRequest} from a key that doesn't expect any arguments.
     *
     * @param key to construct {@link TranslationRequest} for
     * @param <Key> Type of key
     * @return the {@link TranslationRequest}
     */
    public static <Key extends TranslationKey<Key, Void>> TranslationRequest<Key, Void> of(
            final Key key) {
        return of(key, null);
    }

    /**
     * Create a {@link TranslationRequest} from a key that expects multiple arguments.
     *
     * @param key to construct {@link TranslationRequest} for
     * @param args to construct {@link TranslationRequest} for
     * @param <Key> Type of key
     * @param <Arg> Type of Arg
     * @return the {@link TranslationRequest}
     */
    @SafeVarargs
    public static <Key extends TranslationKey<Key, ? super Arg[]>, Arg> TranslationRequest<Key, Arg[]> of(Key key, Arg... args) {
        return new TranslationRequest<>(key, copyOf(args, args.length));
    }

    /**
     * Create a {@link TranslationRequest} from a key that expects an argument.
     *
     * @param key to construct {@link TranslationRequest} for
     * @param args to construct {@link TranslationRequest} for
     * @param <Key> Type of key
     * @param <Arg> Type of Arg
     * @return the {@link TranslationRequest}
     */
    public static <Key extends TranslationKey<Key, ? super Arg>, Arg> TranslationRequest<Key, Arg> of(Key key, Arg arg) {
        return new TranslationRequest<>(key, arg);
    }

    public final Key key() {
        return key;
    }

    public final Arg arg() {
        return arg;
    }

    @Override
    public String toString() {
        return "TranslationRequest{"
                + "key=" + key.toKeyString()
                + (arg == null ? "" : ", arg=" + ObjectUtils.toString(arg))
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || TranslationRequest.class != o.getClass()) return false;
        TranslationRequest<?, ?> that = (TranslationRequest<?, ?>) o;
        return Objects.equals(this.key, that.key)
                && ObjectUtils.equals(this.arg, that.arg);
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 31
            + (arg != null ? arg.hashCode() : 0);
    }
}
