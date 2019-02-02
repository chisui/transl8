package com.github.chisui.translate;

import org.junit.Test;

import static com.github.chisui.translate.VerifyArguments.asPredicate;
import static org.assertj.core.api.Assertions.assertThat;

public class VerifyArgumentsTest {

    @Test
    public void testAsPredicate() {
        assertThat(asPredicate(t -> {
            assertThat(t)
                    .isSameAs(String.class);
            return true;
        }).test(String.class))
                .isTrue();
    }
}