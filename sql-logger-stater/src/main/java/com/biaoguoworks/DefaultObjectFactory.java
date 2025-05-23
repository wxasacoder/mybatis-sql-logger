package com.biaoguoworks;

import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.Reflector;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultObjectFactory implements Serializable {

  private static final long serialVersionUID = -8855120656740914948L;


  public static  <T> T create(Class<T> type) {
    return create(type, null, null);
  }


  public static  <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
    Class<?> classToCreate = resolveInterface(type);
    // we know types are assignable
    return (T) instantiateClass(classToCreate, constructorArgTypes, constructorArgs);
  }

  private static  <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
    try {
      Constructor<T> constructor;
      if (constructorArgTypes == null || constructorArgs == null) {
        constructor = type.getDeclaredConstructor();
        try {
          return constructor.newInstance();
        } catch (IllegalAccessException e) {
          if (Reflector.canControlMemberAccessible()) {
            constructor.setAccessible(true);
            return constructor.newInstance();
          }
          throw e;
        }
      }
      constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]));
      try {
        return constructor.newInstance(constructorArgs.toArray(new Object[0]));
      } catch (IllegalAccessException e) {
        if (Reflector.canControlMemberAccessible()) {
          constructor.setAccessible(true);
          return constructor.newInstance(constructorArgs.toArray(new Object[0]));
        }
        throw e;
      }
    } catch (Exception e) {
      String argTypes = Optional.ofNullable(constructorArgTypes).orElseGet(Collections::emptyList).stream()
          .map(Class::getSimpleName).collect(Collectors.joining(","));
      String argValues = Optional.ofNullable(constructorArgs).orElseGet(Collections::emptyList).stream()
          .map(String::valueOf).collect(Collectors.joining(","));
      throw new ReflectionException("Error instantiating " + type + " with invalid types (" + argTypes + ") or values ("
          + argValues + "). Cause: " + e, e);
    }
  }

  protected static Class<?> resolveInterface(Class<?> type) {
    Class<?> classToCreate;
    if (type == List.class || type == Collection.class || type == Iterable.class) {
      classToCreate = ArrayList.class;
    } else if (type == Map.class) {
      classToCreate = HashMap.class;
    } else if (type == SortedSet.class) { // issue #510 Collections Support
      classToCreate = TreeSet.class;
    } else if (type == Set.class) {
      classToCreate = HashSet.class;
    } else {
      classToCreate = type;
    }
    return classToCreate;
  }

  public static <T> boolean isCollection(Class<T> type) {
    return Collection.class.isAssignableFrom(type);
  }

}
