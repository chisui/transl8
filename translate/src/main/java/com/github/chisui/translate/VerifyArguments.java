package com.github.chisui.translate;

import java.lang.reflect.Type;

public interface VerifyArguments {
    boolean acceptsArgumentsOfType(Type type);
}
