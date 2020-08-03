package com.myownb3.piranha.core.grid.gridelement.wall;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement.WallGridElementBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class WallGridElementTest {

   @Test
   void testOnCollision() {

      // Given
      ProjectileGridElement gridElement = mock(ProjectileGridElement.class);
      when(gridElement.getDamage()).thenReturn(DamageImpl.of(10));
      Position position = Positions.of(5, 5);
      WallGridElement wallGridElement = WallGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(position)
                  .build())
            .build();

      // When
      wallGridElement.onCollision(Collections.singletonList(gridElement));

      // Then
      verify(gridElement).getDamage();
   }

   @Test
   void testIsDestroyed() {
      // Given
      boolean expectedIsDestroyed = true;
      Position position = Positions.of(5, 5);
      WallGridElement wallGridElement = WallGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(position)
                  .build())
            .withHealth(0)
            .build();

      // When
      List<GridElement> gridElements = Collections.singletonList(mock(Obstacle.class));
      wallGridElement.onCollision(gridElements);
      boolean actualIsDestroyed = wallGridElement.isDestroyed();

      // Then
      assertThat(actualIsDestroyed, is(expectedIsDestroyed));
   }

   @Test
   void testIsNotDestroyed() {
      // Given
      boolean expectedIsDestroyed = false;
      Position position = Positions.of(5, 5);
      WallGridElement wallGridElement = WallGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(position)
                  .build())
            .build();

      // When
      boolean actualIsDestroyed = wallGridElement.isDestroyed();

      // Then
      assertThat(actualIsDestroyed, is(expectedIsDestroyed));
   }
}
