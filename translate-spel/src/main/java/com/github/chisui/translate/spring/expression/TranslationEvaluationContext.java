package com.github.chisui.translate.spring.expression;

import com.github.chisui.translate.Translatable;
import com.github.chisui.translate.TranslationFunction;
import com.github.chisui.translate.TranslationKey;
import com.github.chisui.translate.TranslationRequest;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.Objects;

import static com.github.chisui.translate.ObjectUtils.toClass;

public final class TranslationEvaluationContext<A> extends StandardEvaluationContext {

    private final TranslationFunction<String> translate;

    public TranslationEvaluationContext(TranslationFunction<String> translate, A a) {
        super(a);
        this.translate = Objects.requireNonNull(translate, "TranslationEvaluationContext.translate");
        addMethodResolver((ctx0, target0, name, argTypes) ->
                "translate".equals(name) && target0 == getRootObject().getValue()
                    ? (ctx1, target1, args) -> new TypedValue(translate(args))
                    : null);
    }

    private String translate(Object... args) {
        if (args.length == 1) {
            if (args[0] instanceof Translatable) {
                return translate.apply((Translatable) args[0]);
            } else if (args[0] instanceof TranslationRequest) {
                return translate.apply((TranslationRequest) args[0]);
            } else if (args[0] instanceof TranslationKey) {
                TranslationKey key = (TranslationKey) args[0];
                if (Void.class == key.argType()) {
                    return translate.apply(key);
                } else if (toClass(key.argType()).isArray()) {
                    return translate.apply(key, new Object[0]);
                }
            }
        } else if (args[0] instanceof TranslationKey) {
            TranslationKey key = (TranslationKey) args[0];
            if (toClass(key.argType()).isArray()) {
                return translate.apply(key, args);
            } else if (args.length == 2) {
                return translate.apply(key, args[1]);
            }
        }
        throw new UnsupportedOperationException("Can not call 'translate' with arguments " + Arrays.toString(args));
    }

    public static <A> TranslationEvaluationContext<A> of(TranslationFunction<String> translate, A a) {
       return new TranslationEvaluationContext<>(translate, a);
    }
}
