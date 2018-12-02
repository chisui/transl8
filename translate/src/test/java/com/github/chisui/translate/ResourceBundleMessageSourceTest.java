package com.github.chisui.translate;

import org.junit.Test;

import java.util.MissingResourceException;
import java.util.Optional;

import static java.util.Locale.ENGLISH;
import static org.junit.Assert.assertEquals;

public class ResourceBundleMessageSourceTest {

    final ResourceBundleMessageSource classUnderTest = ResourceBundleMessageSource.of("testBundle");

    @Test
    public void testKeyPresent() {
        assertEquals(Optional.of("hello"), classUnderTest.apply("hello", ENGLISH));
    }

    @Test
    public void testKeyAbsent() {
        assertEquals(Optional.empty(), classUnderTest.apply("goodbye", ENGLISH));
    }

    @Test
    public void testMissingBundle() {
        assertEquals(Optional.empty(), ResourceBundleMessageSource.of("someMissingBundle").apply("hello", ENGLISH));
    }
}