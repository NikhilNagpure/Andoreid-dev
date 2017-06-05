package com.heniktechnology.hncore.dynamic_data_base.naming;

import java.lang.reflect.Field;

/**
 * Created by melbic on 21/08/14.
 */
public class FieldNamingStrategy implements ColumnNamingStrategy {
    @Override
    public String translateName(Field f) {
        return f.getName();
    }
}
