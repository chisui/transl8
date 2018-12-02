package com.github.chisui.translate.verify.example;

import com.github.chisui.translate.EnumTranslationKey;
import com.github.chisui.translate.TranslationOverride;

@TranslationOverride(prefix = "override.enum.prefix")
public enum SomeEnumWithPrefixOverride implements EnumTranslationKey<SomeEnumWithPrefixOverride, String> {
    CONST0, CONST1,
}
