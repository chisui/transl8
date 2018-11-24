package com.github.chisui.translate;

public interface KeyToString {

    <SELF extends TranslationKey<SELF, A>, A> String toKeyString(TranslationKey<SELF, A> key);

}
