package com.sma11new.exp.shiro.attack.payloads.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Dependencies {
    public String[] value() default {};

    public static class Utils {
        public static String[] getDependencies(AnnotatedElement annotated) {
            Dependencies deps = annotated.getAnnotation(Dependencies.class);
            if (deps != null && deps.value() != null) {
                return deps.value();
            }
            return new String[0];
        }

        public static String[] getDependenciesSimple(AnnotatedElement annotated) {
            String[] deps = Utils.getDependencies(annotated);
            String[] simple = new String[deps.length];
            for (int i = 0; i < simple.length; ++i) {
                simple[i] = deps[i].split(":", 2)[1];
            }
            return simple;
        }
    }
}

