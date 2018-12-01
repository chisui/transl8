package com.github.chisui.translate;

import java.lang.reflect.Type;

public interface TranslationKey<SELF extends TranslationKey<SELF, A>, A> {

    Type argType();

    String toKeyString();

    @SuppressWarnings({
            "unchecked", // c.getClass() always returns a Class<C>, but java's type system sucks bigtime.
    })
    static <C extends Translatable> ClassTranslationKey<C> of(C c) {
        return of((Class<C>) c.getClass());
    }

    static <C extends Translatable> ClassTranslationKey<C> of(Class<C> cls) {
        return new ClassTranslationKey<>(cls);
    }
}
