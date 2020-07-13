package com.myownb3.piranha.util;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class ObjectUtils {
   private ObjectUtils() {
      // private
   }

   public static <T> T firstNonNull(T value1, T value2) {
      return nonNull(value1) ? value1 : requireNonNull(value2, "Second value must not be null!");
   }
}
