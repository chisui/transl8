package com.github.chisui.translate.verify;

import com.github.chisui.translate.TranslationKey;
import com.github.chisui.translate.verify.example.SomeEnum;
import com.github.chisui.translate.verify.example.SomeTranslatableClass;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static com.github.chisui.translate.verify.TranslationVerify.discoverTranslationKeys;
import static org.assertj.core.api.Assertions.assertThat;


public class TranslationVerifyTest {

    @Test
    public void testDiscoverKeys() {
        assertThat(discoverTranslationKeys("com.github.chisui.translate.verify.example"))
                .isEqualTo(ImmutableSet.of(
                        SomeEnum.CONST0,
                        SomeEnum.CONST1,
                        TranslationKey.of(SomeTranslatableClass.class)
                ));
    }
}