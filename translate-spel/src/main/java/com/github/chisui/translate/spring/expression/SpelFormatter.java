package com.github.chisui.translate.spring.expression;

import com.github.chisui.translate.Formatter;
import com.github.chisui.translate.TranslationFunction;
import org.springframework.expression.Expression;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

public final class SpelFormatter<A> implements Formatter<A, String> {

    private final Expression expr;

    public SpelFormatter(Expression expr) {
        this.expr = requireNonNull(expr, "SpelFormatter.expr");
    }

    @Override
    public boolean acceptsArgumentsOfType(Type type) {
        return SpelUtils.acceptsArgumentsOfType(expr, type);
    }

    @Override
    public String apply(TranslationFunction<String> translate, A a) {
        return expr.getValue(TranslationEvaluationContext.of(translate, a), String.class);
    }
}
