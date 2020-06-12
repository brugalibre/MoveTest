package com.myownb3.piranha.core.grid.gridelement;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.Rebel;
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;

class MoveableObstacleImplTest {

   @Test
   void testIfRebelIsEnemyToAObstacleImpl() {
      // Given
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(Positions.of(0, 0))
            .build();

      // When
      boolean actualIsEnemy = moveable.isEnemy(new Rebel());

      // Then
      assertThat(actualIsEnemy, is(true));
   }

   @Test
   void testMoveableObstacleImpl_WithDestructiveHelper_GetsDestroyed() {

      // Given
      Grid grid = mock(Grid.class);
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(0, 0))
            .build();

      // When
      List<GridElement> gridElements = Collections.singletonList(mockProjectileGridElementent(Integer.MAX_VALUE));
      moveable.onCollision(gridElements);

      // Then
      verify(grid).remove(eq(moveable));
   }

   @Test
   void testMoveableObstacleImpl_WithDestructiveHelper() {

      // Given
      DestructionHelper destructionHelper = mock(DestructionHelper.class);
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(Positions.of(0, 0))
            .withDamage(3)
            .withHealth(50)
            .withDestructionHelper(destructionHelper)
            .build();

      // When
      moveable.isDestroyed();
      moveable.onCollision(Collections.emptyList());

      // Then
      verify(destructionHelper).isDestroyed();
      verify(destructionHelper).onCollision(eq(Collections.emptyList()));
   }

   @Test
   void testAutoDetectMoveableObstacleImpl() {

      // Given
      Grid grid = mock(Grid.class);
      when(grid.moveForward(any())).thenReturn(Positions.of(1, 0));
      MoveableObstacleImpl moveable = spy(MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 0))
                  .build())
            .withPosition(Positions.of(0, 1))
            .build());

      // When
      moveable.autodetect();

      // Then
      verify(moveable).moveForward(eq(10));
   }

   @Test
   void testMoveableObstacleImpl() {

      // Given
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(Positions.of(0, 0))
            .build();

      // When
      boolean isObstacle = Obstacle.class.isAssignableFrom(moveable.getClass());

      // Then
      assertThat(isObstacle, is(true));
   }

   @Test
   void testMoveableObstacleImpl_WithShape() {

      // Given
      Position startPos = Positions.of(0, 0);
      PositionShape shape = PositionShapeBuilder.builder()
            .withPosition(startPos)
            .build();

      // When
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(Positions.of(0, 0))
            .withShape(shape)
            .build();

      // Then
      assertThat(moveable.getShape(), is(shape));
   }

   private ProjectileGridElement mockProjectileGridElementent(double damage) {
      ProjectileGridElement projectile = mock(ProjectileGridElement.class);
      when(projectile.getDamage()).thenReturn(DamageImpl.of(damage));
      return projectile;
   }
}
