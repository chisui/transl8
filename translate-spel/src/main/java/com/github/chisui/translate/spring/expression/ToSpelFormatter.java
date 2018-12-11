package com.github.chisui.translate.spring.expression;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Locale;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public final class ToSpelFormatter implements BiFunction<String, Locale, SpelFormatter<Object>> {

    private final ExpressionParser parser;
    private final ParserContext ctx;

    private ToSpelFormatter(ExpressionParser parser, ParserContext ctx) {
        this.parser = requireNonNull(parser, "ToSpelFormatter.parser");
        this.ctx = requireNonNull(ctx, "ToSpelFormatter.ctx");
    }

    public static ToSpelFormatter of(ExpressionParser parser, ParserContext ctx) {
        return new ToSpelFormatter(parser, ctx);
    }

    public static ToSpelFormatter of() {
        return of(new SpelExpressionParser(), new TemplateParserContext());
    }

    @Override
    public SpelFormatter<Object> apply(String s, Locale locale) {
        return new SpelFormatter<>(parser.parseExpression(s, ctx));
    }
}
