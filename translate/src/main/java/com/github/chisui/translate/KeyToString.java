package com.github.chisui.translate;

import java.util.function.Function;

public interface KeyToString extends Function<TranslationKey<?, ?>, String> {

    <SELF extends TranslationKey<SELF, A>, A> String toKeyString(TranslationKey<SELF, A> key);

    default String apply(TranslationKey<?, ?> key) {
        return toKeyString(key);
    }

}
