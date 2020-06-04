package com.myownb3.piranha.core.grid.gridelement;

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

import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.destruction.SelfDestructive;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;

class ObstacleImplTest {

   @Test
   void testMoveableObstacleImpl_WithDestructiveHelper_GetsDestroyed() {

      // Given
      OnDestroyedCallbackHandler onDestroyedCallbackHandler = mock(OnDestroyedCallbackHandler.class);

      Obstacle obstacle = ((ObstacleBuilder) ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(mock(Position.class)))
                  .withDestructionHelper(DestructionHelperBuilder.builder()
                        .withDamage(DamageImpl.of(1))
                        .withHealth(HealthImpl.of(3))
                        .withSelfDestructiveDamage(SelfDestructive.of(1))
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
            .withPosition(mock(Position.class))
            .withShape(Mockito.mock(CircleImpl.class))
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
      Position position = Mockito.mock(Position.class);
      CircleImpl shape = Mockito.mock(CircleImpl.class);

      // When
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(position)
            .withShape(shape)
            .build();

      // Then
      assertThat(obstacle.getShape(), is(shape));
   }

   private ProjectileGridElement mockProjectileGridElementent(double damage) {
      ProjectileGridElement projectile = mock(ProjectileGridElement.class);
      when(projectile.getDamage()).thenReturn(DamageImpl.of(damage));
      return projectile;
   }
}
