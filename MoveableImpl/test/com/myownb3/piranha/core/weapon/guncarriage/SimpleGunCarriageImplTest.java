package com.myownb3.piranha.core.weapon.guncarriage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;

class SimpleGunCarriageImplTest {

   @Test
   void testGunCarriageTurn90DegreesRight() {

      // Given
      double degree = 90;
      double radius = 5.0;
      int height = 10;
      Position pos = Positions.of(radius, radius);
      Position expectedPos = Positions.of(radius, radius).rotate(degree);
      Position expectedGunPos = Positions.movePositionForward4Distance(expectedPos, radius + height / 2);
      GunCarriage gunCarriage = SimpleGunCarriageBuilder.builder()
            .withRotationSpeed(90)
            .withGun(BulletGunBuilder.builder()
                  .withRectangle(RectangleBuilder.builder()
                        .withHeight(height)
                        .withWidth(height)
                        .withCenter(pos)
                        .withOrientation(Orientation.VERTICAL)
                        .build())
                  .withGunConfig(GunConfigBuilder.builder()
                        .withRoundsPerMinute(5)
                        .withSalveSize(1)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimension(new DimensionImpl(0, 0, 5, 5))
                              .build())
                        .withVelocity(1)
                        .build())
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius((int) radius)
                  .withAmountOfPoints((int) radius)
                  .withCenter(pos)
                  .build())
            .build();

      // When
      gunCarriage.aimTargetPos(expectedGunPos);

      // Then
      assertThat(gunCarriage.getShape().getCenter(), is(expectedPos));
      assertThat(gunCarriage.getGun().getShape().getCenter(), is(expectedGunPos));
   }
}
