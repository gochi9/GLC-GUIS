package com.deadshotmdf.GLC_GUIS.SpecialChunkBlocks.Reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectMethod<T>{
    private final Method method;

    public ReflectMethod(final ClassInfo classInfo, final String methodName, final ClassInfo... parameterTypes) {
        this(classInfo.findClass(), methodName, parameterTypes);
    }

    public ReflectMethod(final ClassInfo classInfo, final int methodOrder, final ClassInfo... parameterTypes) {
        this(classInfo.findClass(), methodOrder, parameterTypes);
    }

    public ReflectMethod(final ClassInfo classInfo, final String methodName, final Class<?>... parameterTypes) {
        this(classInfo.findClass(), methodName, (Class[])parameterTypes);
    }

    public ReflectMethod(final ClassInfo classInfo, final int methodOrder, final Class<?>... parameterTypes) {
        this(classInfo.findClass(), methodOrder, (Class[])parameterTypes);
    }

    public ReflectMethod(final Class<?> clazz, final String methodName) {
        this(clazz, methodName, (Class[])new Class[0]);
    }

    public ReflectMethod(final Class<?> clazz, final int methodOrder) {
        this(clazz, methodOrder, (Class[])new Class[0]);
    }

    public ReflectMethod(final Class<?> clazz, final String methodName, final ClassInfo... parameterTypes) {
        this(clazz, methodName, (Class[])ClassInfo.findClasses(parameterTypes));
    }

    public ReflectMethod(final Class<?> clazz, final int methodOrder, final ClassInfo... parameterTypes) {
        this(clazz, methodOrder, (Class[])ClassInfo.findClasses(parameterTypes));
    }

    public ReflectMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
        this(clazz, null, methodName, (Class[])parameterTypes);
    }

    public ReflectMethod(final Class<?> clazz, final int methodOrder, final Class<?>... parameterTypes) {
        this(clazz, null, methodOrder, (Class[])parameterTypes);
    }

    public ReflectMethod(final Class<?> clazz, final Class<?> returnType, final String methodName, final Class<?>... parameterTypes) {
        this.method = getMethodByName(clazz, methodName, returnType, parameterTypes);
    }

    public ReflectMethod(final Class<?> clazz, final Class<?> returnType, final int methodOrder, final Class<?>... parameterTypes) {
        this.method = getMethodByIndex(clazz, methodOrder, returnType, parameterTypes);
    }

    public T invoke(final Object instance, final Object... args) {
        return this.invokeWithDef(instance, null, args);
    }

    public T invokeWithDef(final Object instance, final T def, final Object... args) {
        Object result = null;
        try {
            if (this.isValid()) {
                result = this.method.invoke(instance, args);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return (T)((result == null) ? def : result);
    }

    public boolean isValid() {
        return this.method != null;
    }

    private static Method getMethodByName(final Class<?> clazz, final String methodName, final Class<?> returnType, final Class<?>... parameterTypes) {
        Method method = null;
        if (clazz != null) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                if (returnType != null && !returnType.isAssignableFrom(method.getReturnType())) {
                    method = null;
                }
                else {
                    method.setAccessible(true);
                }
            }
            catch (Exception ex) {}
        }
        return method;
    }

    private static Method getMethodByIndex(final Class<?> clazz, final int methodOrder, final Class<?> returnType, final Class<?>... parameterTypes) {
        if (clazz != null) {
            int similarMethods = 0;
            for (final Method method : clazz.getDeclaredMethods()) {
                if (returnType == null || method.getReturnType().equals(returnType)) {
                    if (Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                        if (++similarMethods == methodOrder) {
                            method.setAccessible(true);
                            return method;
                        }
                    }
                }
            }
        }
        return null;
    }
}
