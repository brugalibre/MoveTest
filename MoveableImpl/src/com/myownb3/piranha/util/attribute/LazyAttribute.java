package com.myownb3.piranha.util.attribute;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

/**
 * A {@link LazyAttribute} is an wrapper for an attribute which provides a layz access to it's actual value
 * Which means the value is evaluated only if it's required.
 * As soon as it's evaluated once, the same value can be accessed again without calling the potential expensive {@link Supplier} again
 * 
 * @author Dominic
 *
 * @param <T>
 */
public class LazyAttribute<T> {
   private T value;
   private Supplier<T> supplier;

   public LazyAttribute(Supplier<T> supplier) {
      this.supplier = requireNonNull(supplier);
   }

   /**
    * @return the value of this attribute
    */
   public T get() {
      if (nonNull(value)) {
         return value;
      }
      value = supplier.get();
      return value;
   }

   @Override
   public String toString() {
      return get().toString();
   }
}
