package com.myownb3.piranha.core.collision.bounce.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;

class BouncableLookupTableTest {

   @Test
   void testIsNotBouncable_Tank() {
      // Given

      Obstacle obstacle = mock(Obstacle.class);
      TankGridElement movedGridElement = mock(TankGridElement.class);
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
   void testIsBouncable_ProjectileWithWall() {
      // Given

      Wall gridElement = mock(Wall.class);
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
