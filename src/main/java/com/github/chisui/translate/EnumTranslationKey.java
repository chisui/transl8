package com.github.chisui.translate;

public interface EnumTranslationKey<SELF extends Enum<SELF> & TranslationKey<SELF, A>, A> extends TranslationKey<SELF, A> {

    @SuppressWarnings({
            "unchecked", // is checked as long as SELF is a selfreferential type
    })
    default String toKeyString() {
        return getClass().getCanonicalName() + "." + ((Enum<SELF>) this).name();
    }

}
