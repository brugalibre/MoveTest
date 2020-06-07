package com.myownb3.piranha.core.collision.bounce.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.tank.TankGridElement;

class BouncableLookupTableTest {

   @Test
   void testIsBouncable_ProjectileWithSimpleGridElement() {
      // Given

      Obstacle obstacle = mock(Obstacle.class);
      when(obstacle.isAimable()).thenReturn(true);
      ProjectileGridElement movedGridElement = mock(ProjectileGridElement.class);
      boolean expectedIsBouncable = false;

      // When
      boolean actualIsBouncable = BouncableLookupTable.isBouncable(movedGridElement, obstacle);

      // Then
      assertThat(actualIsBouncable, is(expectedIsBouncable));
   }

   @Test
   void testIsBouncable_GridElementWithObstacle() {
      // Given

      Obstacle gridElement = mock(Obstacle.class);
      GridElement movedGridElement = mock(GridElement.class);
      boolean expectedIsBouncable = true;

      // When
      boolean actualIsBouncable = BouncableLookupTable.isBouncable(movedGridElement, gridElement);

      // Then
      assertThat(actualIsBouncable, is(expectedIsBouncable));
   }

   @Test
   void testIsBouncable_ProjectileWithProjectile() {
      // Given

      ProjectileGridElement gridElement = mock(ProjectileGridElement.class);
      ProjectileGridElement movedGridElement = mock(ProjectileGridElement.class);
      boolean expectedIsBouncable = false;

      // When
      boolean actualIsBouncable = BouncableLookupTable.isBouncable(movedGridElement, gridElement);

      // Then
      assertThat(actualIsBouncable, is(expectedIsBouncable));
   }

   @Test
   void testIsBouncable_ProjectileWithGridElement() {
      // Given

      GridElement gridElement = mock(GridElement.class);
      GridElement movedGridElement = mock(ProjectileGridElement.class);
      boolean expectedIsBouncable = true;

      // When
      boolean actualIsBouncable = BouncableLookupTable.isBouncable(movedGridElement, gridElement);

      // Then
      assertThat(actualIsBouncable, is(expectedIsBouncable));
   }

   @Test
   void testIsBouncable_ProjectileWithTank() {
      // Given

      GridElement gridElement = mock(TankGridElement.class);
      GridElement movedGridElement = mock(ProjectileGridElement.class);
      boolean expectedIsBouncable = true;

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
