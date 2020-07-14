package com.myownb3.piranha.core.grid.gridelement.obstacle;


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

import com.myownb3.piranha.core.battle.belligerent.rebelalliance.Rebel;
import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class MoveableObstacleImplTest {

   @Test
   void testIfRebelIsEnemyToAObstacleImpl() {
      // Given
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(8)
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(8)
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
            .withDamage(3)
            .withHealth(50)
            .withDestructionHelper(destructionHelper)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(8)
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
      int velocity = 8;
      MoveableObstacleImpl moveable = spy(MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 1))
                  .build())
            .withVelocity(velocity)
            .build());

      // When
      moveable.autodetect();

      // Then
      verify(moveable).moveForward();
   }

   @Test
   void testMoveableObstacleImpl() {

      // Given
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(8)
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
            .withShape(shape)
            .withVelocity(8)
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
