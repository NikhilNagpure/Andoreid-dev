package com.heniktechnology.hncore.dynamic_data_base.util;


import com.heniktechnology.hncore.dynamic_data_base.Model;

import java.lang.reflect.Field;

/**
 * Created by melbic on 25/08/14.
 */
public abstract class ModelUtils {
    /**
     * Check if a field is a Foreignkey
     * @param field
     * @return
     */
    public static boolean isForeignKey(Field field) {
        return Model.class.isAssignableFrom(field.getType());
    }
}
