package com.myownb3.piranha.core.collision.bounce.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;

class BouncableLookupTableTest {

   @Test
   void testIsBouncable_ProjectileWithMoveable() {
      // Given

      GridElement gridElement = mock(GridElement.class);
      GridElement movedGridElement = mock(ProjectileGridElement.class);
      boolean expectedIsBouncable = false;

      // When
      boolean actualIsBouncable = BouncableLookupTable.isBouncable(movedGridElement, gridElement);

      // Then
      assertThat(actualIsBouncable, is(expectedIsBouncable));
   }

   @Test
   void testIsBouncable_MoveableWithProjectile() {
      // Given

      GridElement gridElement = mock(ProjectileGridElement.class);
      GridElement movedGridElement = mock(GridElement.class);
      boolean expectedIsBouncable = false;

      // When
      boolean actualIsBouncable = BouncableLookupTable.isBouncable(movedGridElement, gridElement);

      // Then
      assertThat(actualIsBouncable, is(expectedIsBouncable));
   }

   @Test
   void testIsBouncable_MoveableWithMoveable() {
      // Given

      GridElement gridElement = mock(GridElement.class);
      GridElement movedGridElement = mock(GridElement.class);
      boolean expectedIsBouncable = true;

      // When
      boolean actualIsBouncable = BouncableLookupTable.isBouncable(movedGridElement, gridElement);

      // Then
      assertThat(actualIsBouncable, is(expectedIsBouncable));
   }

}
