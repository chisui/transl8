package com.github.chisui.translate;

public interface TranslationKey<SELF extends TranslationKey<SELF, A>, A> {

    String toKeyString();

    @SuppressWarnings({
            "unchecked", // c.getClass() always returns a Class<C>, but java's type system sucks bigtime.
    })
    static <C> ClassTranslationKey<C> of(C c) {
        return new ClassTranslationKey<>((Class<C>) c.getClass());
    }

    static <C> ClassTranslationKey<C> of(Class<C> cls) {
        return new ClassTranslationKey<>(cls);
    }
}
