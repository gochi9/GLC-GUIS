package com.deadshotmdf.GLC_GUIS.General.Buttons;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ButtonIdentifier {
    String value();
}
