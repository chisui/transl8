package com.github.chisui.translate;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static com.github.chisui.translate.TranslationFunction.fromUnsafe;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class MessageFormatFormatterTest {

    final TranslationFunction<String> neverInvoced = fromUnsafe((key, arg) -> {
        throw new IllegalArgumentException("should never be invoced but was with args: " + key + " " + arg);
    });

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(MessageFormatFormatter.class)
                .usingGetClass()
                .suppress(NULL_FIELDS)
                .verify();
    }

    Object[][] args() {
        return new Object[][] {
                { "{0} {1,number,#.00} {2}", "a 12.00 b", new Object[]{ "a", 12, "b" } },
                { "{0} {1,number,#.00} {2}", "a 12.00 b", asList("a", 12, "b") },
                { "{0} {1,number,#.00} {2}", "a 12.00 b", ((Iterable<Object>) Arrays.<Object>asList("a", 12, "b")::iterator) },
                { "{0} {1}", "1 2", new byte[]{ 1, 2 } },
                { "{0} {1,number,#.00} {2}", "1 12.00 2", new short[]{ 1, 12, 2 } },
                { "{0} {1,number,#.00} {2}", "1 12.00 2", new int[]{ 1, 12, 2 } },
                { "{0} {1,number,#.00} {2}", "1 12.00 2", new long[]{ 1, 12, 2 } },
                { "{0} {1}", "a b", new char[]{ 'a', 'b' } },
                { "{0} {1}", "0 1", new float[]{ 0, 1 } },
                { "{0} {1}", "0 1", new double[]{ 0, 1 } },
                { "{0} {1}", "true false", new boolean[]{ true, false } },
        };
    }

    @Test
    @Parameters(method = "args")
    public void testApply(String format, String expected, Object obj) {
        assertEquals(expected, MessageFormatFormatter.unsafeOf(format)
                .apply(obj, Locale.ENGLISH, neverInvoced));
    }

    @Test
    @Parameters(method = "args")
    public void testAcceptsArgumentsOfType(String unused0, String unused1, Object arg) {
        assertTrue(MessageFormatFormatter.unsafeOf("").acceptsArgumentsOfType(arg.getClass()));
    }

    @Test
    public void testAcceptsArgumentsOfTypeVoid() {
        assertTrue(MessageFormatFormatter.unsafeOf("").acceptsArgumentsOfType(Void.class));
    }

    @Test
    public void testInvocesTranslatorKey() {
        String actual = MessageFormatFormatter.ofIterable("{0}")
                .apply(singletonList(TranslationRequest.of(ExampleKeyEnum.KEY, "hello")), Locale.ENGLISH,
                        fromUnsafe((key, arg) -> {
                            assertEquals(ExampleKeyEnum.KEY, key);
                            assertArrayEquals(new String[] { "hello" }, (String[]) arg);
                            return "world";
                        }));
        assertEquals("world", actual);
    }

    @Test
    public void testInvocesTranslatorTranslatable() {
        String actual = MessageFormatFormatter.ofIterable("{0}")
                .apply(singletonList(new ExampleTranslatable()), Locale.ENGLISH,
                        fromUnsafe((key, arg) -> {
                            assertEquals(TranslationKey.of(ExampleTranslatable.class), key);
                            assertTrue(arg instanceof ExampleTranslatable);
                            return "world";
                        }));
        assertEquals("world", actual);
    }

    @Test
    public void testToString() {
        assertEquals("MessageFormatFormatter(\"{0} {1,number,#.00} {0}\")",
                MessageFormatFormatter.unsafeOf("{0} {1,number,#.00} {0}").toString());
    }

    @Test
    public void testOfGenericArrayCompiles() {
        MessageFormatFormatter.ofGenericArray("")
                .apply(new Object[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfVoidCompiles() {
        MessageFormatFormatter.ofVoid("")
                .apply(null, Locale.ENGLISH, neverInvoced);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfVoidMessageHasArgs() {
        MessageFormatFormatter.ofVoid("{0}");
    }

    @Test
    public void testOfLongArrayCompiles() {
        MessageFormatFormatter.ofLongArray("")
                .apply(new long[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfIntArrayCompiles() {
        MessageFormatFormatter.ofIntArray("")
                .apply(new int[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfShortArrayCompiles() {
        MessageFormatFormatter.ofShortArray("")
                .apply(new short[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfCharArrayCompiles() {
        MessageFormatFormatter.ofCharArray("")
                .apply(new char[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfByteArrayCompiles() {
        MessageFormatFormatter.ofByteArray("")
                .apply(new byte[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfBooleanArrayCompiles() {
        MessageFormatFormatter.ofBooleanArray("")
                .apply(new boolean[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfDoubleArrayCompiles() {
        MessageFormatFormatter.ofDoubleArray("")
                .apply(new double[0], Locale.ENGLISH, neverInvoced);
    }

    @Test
    public void testOfFloatArrayCompiles() {
        MessageFormatFormatter.ofFloatArray("")
                .apply(new float[0], Locale.ENGLISH, neverInvoced);
    }
}