package com.sma11new.exp.shiro.attack.util;

import com.nqzero.permit.Permit;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Reflections {
    public static void setAccessible(AccessibleObject member) {
        Permit.setAccessible(member);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field;
        block2: {
            field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
                Reflections.setAccessible(field);
            } catch (NoSuchFieldException ex) {
                if (clazz.getSuperclass() == null) break block2;
                field = Reflections.getField(clazz.getSuperclass(), fieldName);
            }
        }
        return field;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = Reflections.getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }

    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = Reflections.getField(obj.getClass(), fieldName);
        return field.get(obj);
    }

    public static Constructor<?> getFirstCtor(String name) throws Exception {
        Constructor<?> ctor = Class.forName(name).getDeclaredConstructors()[0];
        Reflections.setAccessible(ctor);
        return ctor;
    }

    public static Object newInstance(String className, Object ... args) throws Exception {
        return Reflections.getFirstCtor(className).newInstance(args);
    }

    public static <T> T createWithoutConstructor(Class<T> classToInstantiate) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return (T)Reflections.createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }

    public static <T> T createWithConstructor(Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes, Object[] consArgs) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        Reflections.setAccessible(objCons);
        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
        Reflections.setAccessible(sc);
        return (T)sc.newInstance(consArgs);
    }
}

