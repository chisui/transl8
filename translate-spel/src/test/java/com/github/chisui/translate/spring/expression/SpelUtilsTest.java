package com.github.chisui.translate.spring.expression;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static com.github.chisui.translate.spring.expression.SpelUtils.acceptsArgumentsOfType;
import static org.junit.Assert.*;

public class SpelUtilsTest {

    private static Expression parse(String str) {
        return new SpelExpressionParser()
                .parseExpression(str, new TemplateParserContext());
    }

    @Test
    public void testMethodRef() {
        assertTrue(acceptsArgumentsOfType(parse("#{toUpperCase()}"), String.class));
    }

    @Test
    public void testMethodRefWrongArgs() {
        assertFalse(acceptsArgumentsOfType(parse("#{toUpperCase(false)}"), String.class));
    }

    @Test
    public void testIndirectMethodRef() {
        assertTrue(acceptsArgumentsOfType(parse("#{#this.toUpperCase()}"), String.class));
    }

    @Test
    public void testProperty() {
        assertTrue(acceptsArgumentsOfType(parse("#{length}"), Object[].class));
    }

    @Test
    public void testPropertyIndirect() {
        assertTrue(acceptsArgumentsOfType(parse("#{#this.length}"), Object[].class));
    }

    public static class HasField {
        public String a;
    }

    @Test
    public void testPropertyField() {
        assertTrue(acceptsArgumentsOfType(parse("#{a}"), HasField.class));
    }

    public interface HasPlainGetter {
        String a();
    }

    @Test
    public void testPropertyGetterPlain() {
        assertTrue(acceptsArgumentsOfType(parse("#{a}"), HasPlainGetter.class));
    }

    public interface HasGetter {
        String getA();
    }

    @Test
    public void testPropertyGetter() {
        assertTrue(acceptsArgumentsOfType(parse("#{a}"), HasGetter.class));
    }

    public interface HasBooleanGetter {
        boolean isA();
    }

    @Test
    public void testPropertyBooleanGetter() {
        assertTrue(acceptsArgumentsOfType(parse("#{a}"), HasBooleanGetter.class));
    }

    @Test
    public void testArrayAccess() {
        assertTrue(acceptsArgumentsOfType(parse("#{#this[12]}"), Object[].class));
    }

    @Test
    public void testArrayAccessCounterexample() {
        assertFalse(acceptsArgumentsOfType(parse("#{#this[12]}"), Object.class));
    }
}