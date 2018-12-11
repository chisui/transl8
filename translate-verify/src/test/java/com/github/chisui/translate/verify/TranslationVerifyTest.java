package com.github.chisui.translate.verify;

import com.github.chisui.translate.*;
import com.github.chisui.translate.verify.example.*;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.chisui.translate.DefaultKeyToString.defaultToKeyString;
import static com.github.chisui.translate.verify.TranslationVerify.allErrors;
import static com.github.chisui.translate.verify.TranslationVerify.allFormatters;
import static com.github.chisui.translate.verify.TranslationVerify.discoverTranslationKeys;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;


public class TranslationVerifyTest {

    public static final ImmutableSet<TranslationKey<? extends TranslationKey<?, ?>, ?>> ALL_KEYS = ImmutableSet.of(
            SomeEnum.CONST0,
            SomeEnum.CONST1,
            SomeEnumWithOverride.CONST0,
            SomeEnumWithOverride.CONST1,
            SomeEnumWithPrefixOverride.CONST0,
            SomeEnumWithPrefixOverride.CONST1,
            TranslationKey.of(SomeTranslatableClass.class),
            TranslationKey.of(SomeTranslatableClassWithOverride.class),
            TranslationKey.of(SomeTranslatableClassWithPrefixOverride.class)
    );

    @Test
    public void testDiscoverKeys() {
        assertThat(discoverTranslationKeys("com.github.chisui.translate.verify.example"))
                .isEqualTo(ALL_KEYS);
    }

    @Test
    public void testAllFormatters() {
        HashBasedTable<Locale, String, Optional<Formatter<?, String>>> table = HashBasedTable.create();
        allFormatters("com.github.chisui.translate.verify.example",
                FormatterSource.of(
                        ResourceBundleMessageSource.of("testBundle"),
                        MessageFormatFormatter::<Object>unsafeOf),
                GERMAN, ENGLISH)
            .forEach(cell -> table.put(cell.getRowKey(), defaultToKeyString(cell.getColumnKey()), cell.getValue()));

        assertThat(table.row(ENGLISH).keySet())
                .containsExactlyInAnyOrderElementsOf(ALL_KEYS.stream()
                        .map(DefaultKeyToString::defaultToKeyString)
                        .collect(toImmutableSet()));
        assertThat(table.row(GERMAN).keySet())
                .containsExactlyInAnyOrderElementsOf(ALL_KEYS.stream()
                        .map(DefaultKeyToString::defaultToKeyString)
                        .collect(toImmutableSet()));

        assertThat(table.get(GERMAN, "override.translatable"))
                .contains(MessageFormatFormatter.unsafeOf("asdf {0}", GERMAN));
        assertThat(table.get(GERMAN, "com.github.chisui.translate.verify.example.SomeEnum.const0"))
                .contains(MessageFormatFormatter.unsafeOf("hello", GERMAN));
    }

    @Test(expected = IllegalStateException.class)
    public void testBrokenEnum() {
        discoverTranslationKeys("com.github.chisui.translate.verify.brokenEnum");
    }
}