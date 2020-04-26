package com.myownb3.piranha.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ObjectUtilsTest {

   @Test
   void testFirstValueNonNull() {

      // Given
      Date date1 = new Date();
      Date date2 = null;
      Date expectedValue = date1;

      // When
      Date actualValue = ObjectUtils.firstNonNull(date1, date2);

      // Then
      Assert.assertThat(actualValue, CoreMatchers.is(expectedValue));
   }

   @Test
   void testSecondValueNonNull() {

      // Given
      Date date1 = null;
      Date date2 = new Date();
      Date expectedValue = date2;

      // When
      Date actualValue = ObjectUtils.firstNonNull(date1, date2);

      // Then
      Assert.assertThat(actualValue, CoreMatchers.is(expectedValue));
   }

   @Test
   void testBothValuesNull() {

      // Given
      Date date1 = null;
      Date date2 = null;

      // When
      Executable ex = () -> {
         ObjectUtils.firstNonNull(date1, date2);
      };
      // Then
      assertThrows(NullPointerException.class, ex);
   }

}
