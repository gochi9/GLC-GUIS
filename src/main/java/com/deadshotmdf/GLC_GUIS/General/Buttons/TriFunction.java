package com.deadshotmdf.GLC_GUIS.General.Buttons;

@FunctionalInterface
public interface TriFunction<A, B, C, D, E, R> {
    R apply(A a, B b, C c, D d, E e);
}
