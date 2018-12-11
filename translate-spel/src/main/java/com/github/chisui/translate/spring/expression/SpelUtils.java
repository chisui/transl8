package com.github.chisui.translate.spring.expression;

import org.springframework.expression.Expression;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.ast.*;
import org.springframework.expression.spel.standard.SpelExpression;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.chisui.translate.ObjectUtils.toClass;

public final class SpelUtils {
    private SpelUtils() {}

    public static boolean acceptsArgumentsOfType(Expression expr, Type type) {
        return aaot(expr, toClass(type)).orElse(false);
    }

    private static Optional<Boolean> aaot(Expression expr, Class<?> type) {
        if (expr instanceof CompositeStringExpression) {
            CompositeStringExpression compExpr = (CompositeStringExpression) expr;
            return Arrays.stream(compExpr.getExpressions())
                    .map(subExpr -> aaot(subExpr, type))
                    .reduce(Optional.empty(), liftOptional2((a, b) -> a && b));
        } else if (expr instanceof LiteralExpression) {
            return Optional.empty();
        } else if (expr instanceof SpelExpression) {
            SpelExpression spelExpr = (SpelExpression) expr;
            return Optional.of(acceptsArgumentsOfType(spelExpr.getAST(), type));
        } else {
            throw new IllegalArgumentException("unknown expression type: " + expr.getClass().getCanonicalName());
        }
    }

    private static boolean acceptsArgumentsOfType(SpelNode ast, Class<?> type) {
        if (ast instanceof CompoundExpression) {
            CompoundExpression compExp = (CompoundExpression) ast;
            SpelNode root = compExp.getChild(0);
            if (root instanceof VariableReference && root.toStringAST().matches("#(this|root)")) {
                return acceptsArgumentsOfType(compExp.getChild(1), type);
            } else {
                return true;
            }
        } else if (ast instanceof PropertyOrFieldReference) {
            PropertyOrFieldReference prop = (PropertyOrFieldReference) ast;
            return hasProp(type, prop.getName());
        } else if (ast instanceof MethodReference) {
            MethodReference ref = (MethodReference) ast;
            return hasMethod(type, ref.getName(), resolveArgTypes(ref));
        } else if (ast instanceof Indexer) {
            Indexer ix = (Indexer) ast;
            return resolveArgType(ix.getChild(0))
                    .map(ixType ->
                        ixType == Integer.class
                            ? type.isArray()
                            : hasMethod(type, "get", Optional.of(type)))
                    .orElseGet(() -> hasMethod(type, "get", Optional.empty()));
        } else {
            return IntStream.range(0, ast.getChildCount())
                    .mapToObj(ast::getChild)
                    .allMatch(child -> acceptsArgumentsOfType(child, type));
        }
    }

    private static boolean hasProp(Class<?> type, String name) {
        if (type.isArray() && "length".equals(name)) {
            return true;
        }
        try {
            type.getField(name);
        } catch (NoSuchFieldException e) {
            try {
                type.getMethod(name);
            } catch (NoSuchMethodException e1) {
                String capitalName = name.substring(0,1).toUpperCase() + name.substring(1);
                try {
                    type.getMethod("get" + capitalName);
                } catch (NoSuchMethodException e2) {
                    try {
                        type.getMethod("is" + capitalName);
                    } catch (NoSuchMethodException e3) {
                        Class<?> superCls = type.getSuperclass();
                        if (superCls != Object.class && superCls != null) {
                            return hasProp(superCls, name);
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static List<Optional<Class<?>>> resolveArgTypes(MethodReference ref) {
        return IntStream.range(0, ref.getChildCount())
                .mapToObj(ref::getChild)
                .map(SpelUtils::resolveArgType)
                .collect(Collectors.toList());
    }

    private static Optional<Class<?>> resolveArgType(SpelNode node) {
        return Optional.of(node)
                .map(lit -> {
                    if (lit instanceof StringLiteral) {
                        return String.class;
                    } else if (lit instanceof BooleanLiteral) {
                        return Boolean.class;
                    } else if (lit instanceof FloatLiteral) {
                        return Float.class;
                    } else if (lit instanceof IntLiteral) {
                        return Integer.class;
                    } else if (lit instanceof RealLiteral) {
                        return Double.class;
                    } else {
                        return null;
                    }
                });
    }

    private static <T> BinaryOperator<Optional<T>> liftOptional2(BinaryOperator<T> f) {
        return (o0, o1) -> o0.flatMap(a -> o1.map(b -> f.apply(a, b)));
    }

    private static boolean hasMethod(Class<?> type, String name, Optional<Class<?>>... knownArgTypes) {
        return hasMethod(type, name, Arrays.asList(knownArgTypes));
    }

    private static boolean hasMethod(Class<?> type, String name, List<Optional<Class<?>>> knownArgTypes) {
        return Arrays.stream(type.getMethods())
                .filter(m -> m.getName().equals(name))
                .map(Method::getParameterTypes)
                .anyMatch(args -> argsMatch(args, knownArgTypes));
    }

    private static boolean argsMatch(Class<?>[] args, List<Optional<Class<?>>> knownArgTypes) {
        if (args.length == knownArgTypes.size()) {
            return IntStream.range(0, args.length)
                    .allMatch(i -> {
                        Class<?> arg = args[i];
                        Optional<Class<?>> expected = knownArgTypes.get(i);

                        return expected
                                .map(arg::isAssignableFrom)
                                .orElse(true);
                    });
        } else {
            return false;
        }
    }
}
