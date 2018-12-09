package com.github.chisui.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static com.github.chisui.translate.EnumTranslationKeyTest.TestEnumKey.HELLO;
import static com.github.chisui.translate.Formatter.*;
import static com.github.chisui.translate.TranslationFunction.fromUnsafe;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class FormatterBuildingTest {

    private static final TestSelfTranslatable TRANSLATABLE = new TestSelfTranslatable();
    private static final TranslationFunction<String> TRANSLATOR = fromUnsafe((k, a) -> {
        if (a == TRANSLATABLE) {
            assertEquals(k, TranslationKey.of((Translatable) a));
            return "translation";
        } else if(k == HELLO) {
            return Arrays.toString(ObjectUtils.toArrayUnsafe(a));
        } else {
            fail("unexpected arguments to translator: " + k + ": " + a);
            return null;
        }
    });
    private static final CollectingFormatter<Pojo, String> POJO_FORMATTER = ofJoining(
            of("Pojo{a="),
            by(Pojo::a, Object::toString),
            of(", t="),
            byTranslatable(Pojo::t),
            of(", strs="),
            byKey(Pojo::strs, HELLO),
            of("}"));
    private static final CollectingFormatter<Deep, String> DEEP_FORMATTER = ofJoining(
            of("Deep{a="),
            by(Deep::a, POJO_FORMATTER),
            of(", b="),
            by(Deep::b, POJO_FORMATTER),
            of("}"));

    private static class Pojo {
        private final Object a;
        private final Translatable t;
        private final String[] strs;

        Pojo(Object a, Translatable t, String... strs) {
            this.a = a;
            this.t = t;
            this.strs = strs;
        }

        Object a() {
            return a;
        }

        Translatable t() {
            return t;
        }

        String[] strs() {
            return strs;
        }
    }

    private static class Deep {
        private final Pojo a, b;

        Deep(Pojo a, Pojo b) {
            this.a = a;
            this.b = b;
        }

        Pojo a() {
            return a;
        }

        Pojo b() {
            return b;
        }
    }

    @Test
    public void testSimple() {
        assertEquals(
                "Pojo{a=str, t=translation, strs=[a, b]}",
                POJO_FORMATTER.apply(TRANSLATOR,
                        new Pojo("str", TRANSLATABLE, "a", "b")));
    }

    @Test
    public void testSubFormatter() {
        assertEquals(
                "Deep{a=Pojo{a=str, t=translation, strs=[a, b]}, b=Pojo{a=otherString, t=translation, strs=[c, d]}}",
                DEEP_FORMATTER.apply(
                        TRANSLATOR,
                        new Deep(
                                new Pojo("str", TRANSLATABLE, "a", "b"),
                                new Pojo("otherString", TRANSLATABLE, "c", "d"))));
    }


    @Test
    @Parameters({
            "false, com.github.chisui.translate.FormatterBuildingTest$Deep",
            "true, com.github.chisui.translate.FormatterBuildingTest$Pojo",
            "false, java.lang.String",
    })
    public void testFormatterVerifyArgs(boolean expected, Class<?> cls) {
        assertEquals(expected, POJO_FORMATTER.acceptsArgumentsOfType(cls));
    }

    @Test
    @Parameters({
            "true, com.github.chisui.translate.FormatterBuildingTest$Deep",
            "false, com.github.chisui.translate.FormatterBuildingTest$Pojo",
            "false, java.lang.String",
    })
    public void testSubFormatterVerifyArgs(boolean expected, Class<?> cls) {
        assertEquals(expected, DEEP_FORMATTER.acceptsArgumentsOfType(cls));
    }
}