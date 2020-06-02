package com.myownb3.piranha.core.weapon.gun.projectile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.BulletImpl.BulletBuilder;

class BulletImplTest {

   @Test
   void testOnCollision_Twice_RemoveDestroyedProjectile() {

      // Given
      Projectile projectile = BulletBuilder.builder()
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
      projectile.onCollision(); // first time
      projectile.onCollision();// second time

      // Then
      assertThat(projectile.isDestroyed(), is(true));
      verify(projectile.getGrid()).remove(eq(projectile));
   }

}
