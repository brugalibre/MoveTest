/**
 * 
 */
package com.myownb3.piranha.core.grid.direction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
class DirectionTest {

   @Test
   public void testCreateDirectionWithForwardXAndY_FirstQuadrant() {

      // Given
      double forwardY = 3;
      double forwardX = 1;
      double expectedAngle = 71.5650511771;

      // When
      Direction direction = Directions.of(forwardX, forwardY);

      // Then
      assertThat(MathUtil.round(direction.getAngle(), 10), is(expectedAngle));
   }

   @Test
   public void testCreateDirectionWithForwardXAndY_SecondQuadrant() {

      // Given
      double forwardX = 1;
      double forwardY = -1;
      double expectedAngle = 315;

      // When
      Direction direction = Directions.of(forwardX, forwardY);

      // Then
      assertThat(MathUtil.round(direction.getAngle(), 10), is(expectedAngle));
   }

   @Test
   public void testCreateDirectionWithForwardXAndY_ThirdQuadrant() {

      // Given
      double forwardX = -3;
      double forwardY = -1;
      double expectedAngle = 180 + 18.4349488229;

      // When
      Direction direction = Directions.of(forwardX, forwardY);

      // Then
      assertThat(MathUtil.round(direction.getAngle(), 10), is(expectedAngle));
   }

   @Test
   void testHashCode() {

      // Given
      Direction north = Directions.N;

      // When
      Direction anotherNorth = new DirectionImpl(90);

      // Then
      MatcherAssert.assertThat(north.hashCode(), is(anotherNorth.hashCode()));
   }

   @Test
   void testHashCodeNoCardinalDirection() {

      // Given
      Direction north = Directions.N;

      // When
      Direction anotherNorth = new DirectionImpl(90, null);

      // Then
      MatcherAssert.assertThat(north.hashCode(), is(not(anotherNorth.hashCode())));
   }

   @Test
   void testEquals() {

      // Given
      Direction north = Directions.N;

      // When
      Direction anotherNorth = new DirectionImpl(90, "N");
      Direction anotherNotExactlyNorth = new DirectionImpl(89, "N");

      // Then
      MatcherAssert.assertThat(north, is(anotherNorth));
      Assert.assertTrue(north.equals(anotherNorth));
      Assert.assertTrue(north.equals(north));
      Assert.assertFalse(north.equals(null));
      Assert.assertFalse(north.equals(new Object()));
      Assert.assertFalse(north.equals(anotherNotExactlyNorth));
      Assert.assertFalse(anotherNorth.equals(anotherNotExactlyNorth));
   }

   @Test
   void testEquals_NoCardinalDirection() {

      // Given
      Direction north = Directions.N;

      // When
      Direction anotherNorth = new DirectionImpl(90, null);

      // Then
      MatcherAssert.assertThat(anotherNorth, is(not(north)));
      MatcherAssert.assertThat(north, is(not(anotherNorth)));
   }

   @Test
   void testEquals_BothNoCardinalDirection() {

      // Given
      Direction north = new DirectionImpl(90, null);

      // When
      Direction anotherNorth = new DirectionImpl(90, null);

      // Then
      MatcherAssert.assertThat(anotherNorth, is(north));
      MatcherAssert.assertThat(north, is(anotherNorth));
   }

   @Test
   void testToString() {

      // Given
      String expectedToString = "Cardinal-Direction:N, Rotation: 90.0";
      // When
      Direction north = Directions.N;

      // Then
      MatcherAssert.assertThat(north.toString(), is(expectedToString));
   }
}
