package com.myownb3.piranha.core.destruction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement;

class DestructionHelperTest {

   @Test
   void testBuildDestructionHelper() {

      // Given
      double damage = 1.0;

      // When
      DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
            .withDamage(DamageImpl.of(damage))
            .withHealth(HealthImpl.of(3))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(3))
            .withOnDestroyedCallbackHandler(mock(OnDestroyedCallbackHandler.class))
            .build();

      // Then
      assertThat(destructionHelper.getDamage().getDamageValue(), is(damage));
   }

   @Test
   void testOnCollisionWithNonDestructiveElements_EnoughSelfDamage() {

      // Given
      DestructionHelper destructionHelper = DestructionHelperBuilder.builder()
            .withDamage(DamageImpl.of(1))
            .withHealth(HealthImpl.of(3))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(3))
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
            .withDamage(DamageImpl.of(1))
            .withHealth(HealthImpl.of(3))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(0.2))
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
            .withDamage(DamageImpl.of(3))
            .withHealth(HealthImpl.of(10))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(0.2))
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
            .withDamage(DamageImpl.of(1))
            .withHealth(HealthImpl.of(10))
            .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(1))
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
