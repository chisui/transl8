package com.github.chisui.translate.verify.example;

import com.github.chisui.translate.EnumTranslationKey;
import com.github.chisui.translate.TranslationOverride;

@TranslationOverride("override.enum")
public enum SomeEnumWithOverride implements EnumTranslationKey<SomeEnumWithOverride, String> {
    CONST0, CONST1,
}
