package com.github.chisui.translate;

import static java.util.Objects.requireNonNull;

public final class ComposedTranslationKey<
        A extends TranslationKeyBase<A>,
        B extends Enum<B> & ComposableWithTranslationKey<B, A, R>,
        R>
    implements TranslationKey<ComposedTranslationKey<A, B, R>, R> {

    private final A a;
    private final B b;

    private ComposedTranslationKey(A a, B b) {
        this.a = requireNonNull(a);
        this.b = requireNonNull(b);
    }

    public static <
            A extends TranslationKeyBase<A>,
            B extends Enum<B> & ComposableWithTranslationKey<B, A, R>,
            R>
        ComposedTranslationKey<A, B, R> of(A a, B b) {
        return new ComposedTranslationKey<>(a, b);
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public String toKeyString() {
        return a.toKeyString() + "." + b.name().toLowerCase();
    }


}
