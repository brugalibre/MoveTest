package com.myownb3.piranha.core.grid.gridelement.obstacle;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.belligerent.rebelalliance.Rebel;
import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;

class ObstacleImplTest {

   @Test
   void testIfRebelIsEnemyToAObstacleImpl() {
      // Given
      ObstacleImpl obstacle = ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(1)
                  .withHealth(3)
                  .withSelfDestructiveDamage(1)
                  .build())
            .withShape(mock(CircleImpl.class))
            .build();

      // When
      boolean actualIsEnemy = obstacle.isEnemy(new Rebel());

      // Then
      assertThat(actualIsEnemy, is(true));
   }

   @Test
   void testMoveableObstacleImpl_WithDestructiveHelper_GetsDestroyed() {

      // Given
      OnDestroyedCallbackHandler onDestroyedCallbackHandler = mock(OnDestroyedCallbackHandler.class);

      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(1)
                  .withHealth(3)
                  .withSelfDestructiveDamage(1)
                  .withOnDestroyedCallbackHandler(onDestroyedCallbackHandler)
                  .build())
            .withShape(mock(CircleImpl.class))
            .build();

      // When
      List<GridElement> gridElements = Collections.singletonList(mock(Obstacle.class));
      obstacle.onCollision(gridElements);

      // Then
      verify(onDestroyedCallbackHandler).onDestroy();
   }

   @Test
   void testMoveableObstacleImpl_WithDestructiveHelper() {

      // Given
      Grid grid = mock(Grid.class);
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(mock(CircleImpl.class))
            .build();

      // When
      obstacle.onCollision(Collections.singletonList(mockProjectileGridElementent(Integer.MAX_VALUE)));

      // Then
      verify(grid).remove(eq(obstacle));
   }

   @Test
   void testConstructor() {

      // Given
      Grid grid = Mockito.mock(Grid.class);
      CircleImpl shape = Mockito.mock(CircleImpl.class);

      // When
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(shape)
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .build();

      // Then
      assertThat(obstacle.getShape(), is(shape));
      assertThat(obstacle.getBelligerentParty(), is(BelligerentPartyConst.REBEL_ALLIANCE));
   }

   private ProjectileGridElement mockProjectileGridElementent(double damage) {
      ProjectileGridElement projectile = mock(ProjectileGridElement.class);
      when(projectile.getDamage()).thenReturn(DamageImpl.of(damage));
      return projectile;
   }
}
