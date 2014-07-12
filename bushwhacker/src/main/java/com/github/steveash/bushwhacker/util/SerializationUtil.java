package com.github.steveash.bushwhacker.util;

import java.lang.reflect.Field;

/**
 * @author Steve Ash
 */
public class SerializationUtil {

  public static <T> FieldSetter<T> getFieldSetter(final Class<T> clazz, String fieldName) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      return new FieldSetter<>(field);
    } catch (NoSuchFieldException e) {
      throw new AssertionError("no field exists in the code with this name, you typed it wrong", e);
    }
  }

  public static final class FieldSetter<T> {

    private final Field field;

    private FieldSetter(Field field) {
      this.field = field;
      field.setAccessible(true);
    }

    public void set(T instance, Object value) {
      try {
        field.set(instance, value);
      } catch (IllegalAccessException impossible) {
        throw new AssertionError(impossible);
      }
    }

    public void set(T instance, int value) {
      try {
        field.set(instance, value);
      } catch (IllegalAccessException impossible) {
        throw new AssertionError(impossible);
      }
    }

    public void set(T instance, boolean value) {
      try {
        field.set(instance, value);
      } catch (IllegalAccessException impossible) {
        throw new AssertionError(impossible);
      }
    }
  }

}
