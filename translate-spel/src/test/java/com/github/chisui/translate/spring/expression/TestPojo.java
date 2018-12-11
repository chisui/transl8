package com.github.chisui.translate.spring.expression;

import com.github.chisui.translate.Translatable;
import com.github.chisui.translate.TranslationOverride;

@TranslationOverride("spel.pojo")
public class TestPojo implements Translatable {
    private final String a;

    TestPojo(String a) {
        this.a = a;
    }

    public String getA() {
        return a;
    }
}
