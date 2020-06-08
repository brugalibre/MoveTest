package com.myownb3.piranha.core.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;

class ProjectileGridElemTest {

   @Test
   void testIsAimable() {

      // Given
      ProjectileGridElement projectileGridElementent = ProjectileGridElementBuilder.builder()
            .withGrid(spy(MirrorGridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .withMinX(0)
                  .withMinY(0)
                  .build()))
            .withPosition(Positions.of(9.5, 8.5))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(9.5, 8.5))
                  .build())
            .build();

      // When

      // Then
      assertThat(projectileGridElementent.isAimable(), is(false));
      assertThat(projectileGridElementent.isAvoidable(), is(true));
   }

   @Test
   void testOnCollision_Twice_RemoveDestroyedProjectile() {

      // Given
      ProjectileGridElement projectileGridElementent = ProjectileGridElementBuilder.builder()
            .withGrid(spy(MirrorGridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .withMinX(0)
                  .withMinY(0)
                  .build()))
            .withPosition(Positions.of(9.5, 8.5))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(9.5, 8.5))
                  .build())
            .build();

      // When
      projectileGridElementent.onCollision(Collections.singletonList(mock(Wall.class))); // first time
      projectileGridElementent.onCollision(Collections.singletonList(mock(Wall.class)));// second time

      // Then
      assertThat(projectileGridElementent.isDestroyed(), is(true));
      verify(projectileGridElementent.getGrid()).remove(eq(projectileGridElementent));
   }

   @Test
   void testOnCollision_Once_NotRemoveDestroyedSinceNotDestroyedProjectile() {

      // Given
      ProjectileGridElement projectileGridElementent = ProjectileGridElementBuilder.builder()
            .withGrid(spy(MirrorGridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .withMinX(0)
                  .withMinY(0)
                  .build()))
            .withHealth(100)
            .withDamage(5)
            .withPosition(Positions.of(9.5, 8.5))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(9.5, 8.5))
                  .build())
            .build();

      // When
      projectileGridElementent.onCollision(Collections.singletonList(mock(Wall.class))); // first time

      // Then
      assertThat(projectileGridElementent.isDestroyed(), is(false));
      verify(projectileGridElementent.getGrid(), never()).remove(eq(projectileGridElementent));
   }

   @Test
   void testOnCollision_Once_RemoveDestroyedSinceNotDestroyedProjectile() {

      // Given
      ProjectileGridElement projectileGridElementent = ProjectileGridElementBuilder.builder()
            .withGrid(spy(MirrorGridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .withMinX(0)
                  .withMinY(0)
                  .build()))
            .withHealth(100)
            .withDamage(5)
            .withPosition(Positions.of(9.5, 8.5))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(9.5, 8.5))
                  .build())
            .build();

      // When
      projectileGridElementent.onCollision(Collections.singletonList(mock(Obstacle.class))); // first time

      // Then
      assertThat(projectileGridElementent.isDestroyed(), is(true));
      verify(projectileGridElementent.getGrid()).remove(eq(projectileGridElementent));
   }
}
