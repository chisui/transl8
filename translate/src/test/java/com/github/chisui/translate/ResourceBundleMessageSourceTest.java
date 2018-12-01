package com.github.chisui.translate;

import org.junit.Test;

import java.util.Optional;

import static java.util.Locale.ENGLISH;
import static org.junit.Assert.*;

public class ResourceBundleMessageSourceTest {

    final ResourceBundleMessageSource classUnderTest = ResourceBundleMessageSource.of("testBundle").get();

    @Test
    public void testKeyPresent() {
        assertEquals(Optional.of("hello"), classUnderTest.apply(ENGLISH, "hello"));
    }

    @Test
    public void testKeyAbsent() {
        assertEquals(Optional.empty(), classUnderTest.apply(ENGLISH, "goodbye"));
    }

    @Test
    public void testMissingBundle() {
        assertEquals(Optional.empty(), ResourceBundleMessageSource.of("someMissingBundle"));
    }
}