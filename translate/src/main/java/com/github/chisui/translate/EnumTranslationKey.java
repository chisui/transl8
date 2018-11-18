package com.github.chisui.translate;

public interface EnumTranslationKey<
    SELF extends Enum<SELF> & EnumTranslationKey<SELF, A>,
    A>
            extends EnumTranslationKeyBase<SELF>, TranslationKey<SELF, A> {
}
