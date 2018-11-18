package com.github.chisui.translate;

public interface ComposableWithTranslationKey<
        SELF extends Enum<SELF> & ComposableWithTranslationKey<SELF, A, R>,
        A extends TranslationKeyBase<A>,
        R> {

    default ComposedTranslationKey<A, SELF, R> compose(A a) {
        return ComposedTranslationKey.of(a, (SELF) this);
    }

}
