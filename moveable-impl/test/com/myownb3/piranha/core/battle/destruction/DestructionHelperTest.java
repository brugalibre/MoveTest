package com.myownb3.piranha.core.battle.destruction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;

class DestructionHelperTest {

   @Test
   void testIsNotDestroyedGridElement() {

      // Given
      GridElement gridElement = mock(GridElement.class);

      // When
      boolean actualIsNotDestroyed = DestructionHelper.isNotDestroyed(gridElement);

      // Then
      assertThat(actualIsNotDestroyed, is(true));
   }

   @Test
   void testIsNotDestroyedDestructible() {

      // Given
      boolean isGridElement1Destroyed = false;
      boolean isGridElement2Destroyed = true;
      Obstacle destructibleGridElement1 = mock(ObstacleImpl.class);
      when(destructibleGridElement1.isDestroyed()).thenReturn(isGridElement1Destroyed);
      Obstacle destructibleGridElement2 = mock(ObstacleImpl.class);
      when(destructibleGridElement2.isDestroyed()).thenReturn(isGridElement2Destroyed);

      // When
      boolean actualIsNotDestroyed1 = DestructionHelper.isNotDestroyed(destructibleGridElement1);
      boolean actualIsNotDestroyed2 = DestructionHelper.isNotDestroyed(destructibleGridElement2);

      // Then
      assertThat(actualIsNotDestroyed1, is(!isGridElement1Destroyed));
      assertThat(actualIsNotDestroyed2, is(!isGridElement2Destroyed));
   }

   @Test
   void testBuildDestructionHelper() {

      // Given
      double damage = 1.0;

      // When
      DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
            .withDamage(damage)
            .withHealth(3)
            .withSelfDestructiveDamage(3)
            .withOnDestroyedCallbackHandler(mock(OnDestroyedCallbackHandler.class))
            .build();

      // Then
      assertThat(destructionHelper.getDamage().getDamageValue(), is(damage));
   }

   @Test
   void testOnCollisionWithNonDestructiveElements_EnoughSelfDamage() {

      // Given
      DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
            .withDamage(1)
            .withHealth(3)
            .withSelfDestructiveDamage(3)
            .withOnDestroyedCallbackHandler(mock(OnDestroyedCallbackHandler.class))
            .build();
      List<GridElement> gridElements = Collections.singletonList(mock(Obstacle.class));

      // When
      destructionHelper.onCollision(gridElements);

      // Then
      assertThat(destructionHelper.isDestroyed(), is(true));
   }

   @Test
   void testOnCollisionWithNonDestructiveElements_NotEnoughSelfDamage() {

      // Given
      DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
            .withDamage(1)
            .withHealth(3)
            .withSelfDestructiveDamage(0.2)
            .withOnDestroyedCallbackHandler(mock(OnDestroyedCallbackHandler.class))
            .build();
      List<GridElement> gridElements = Collections.singletonList(mock(TurretGridElement.class));// Turret is none destructive AND non destructible!

      // When
      destructionHelper.onCollision(gridElements);

      // Then
      assertThat(destructionHelper.isDestroyed(), is(false));
   }

   @Test
   void testProjectileOnCollisionWithGrid_NotDestroyed() {

      // Given
      DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
            .withDamage(3)
            .withHealth(10)
            .withSelfDestructiveDamage(0.2)
            .withOnDestroyedCallbackHandler(mock(OnDestroyedCallbackHandler.class))
            .build();
      List<GridElement> gridElements = Collections.singletonList(mock(Wall.class));

      // When
      destructionHelper.onCollision(gridElements);

      // Then
      assertThat(destructionHelper.isDestroyed(), is(false));
   }

   @Test
   void testOnCollisionWithDestructiveElements_Destroyed() {

      // Given
      DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
            .withDamage(1)
            .withHealth(10)
            .withSelfDestructiveDamage(1)
            .withOnDestroyedCallbackHandler(mock(OnDestroyedCallbackHandler.class))
            .build();
      List<GridElement> gridElements = Collections.singletonList(mockProjectileGridElementent(11));

      // When
      destructionHelper.onCollision(gridElements);

      // Then
      assertThat(destructionHelper.isDestroyed(), is(true));
   }

   private ProjectileGridElement mockProjectileGridElementent(double damage) {
      ProjectileGridElement projectile = mock(ProjectileGridElement.class);
      when(projectile.getDamage()).thenReturn(DamageImpl.of(damage));
      return projectile;
   }

}
