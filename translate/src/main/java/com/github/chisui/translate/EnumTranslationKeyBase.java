package com.github.chisui.translate;

public interface EnumTranslationKeyBase<
            SELF extends Enum<SELF> & EnumTranslationKeyBase<SELF>>
        extends TranslationKeyBase<SELF> {

    @SuppressWarnings({
            "unchecked", // is checked as long as SELF is a selfreferential type
    })
    default String toKeyString() {
        return getClass().getCanonicalName()
                + "."
                + ((Enum<SELF>) this).name().toLowerCase();
    }

}
