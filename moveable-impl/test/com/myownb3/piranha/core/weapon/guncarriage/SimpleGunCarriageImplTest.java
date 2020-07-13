package com.myownb3.piranha.core.weapon.guncarriage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;

class SimpleGunCarriageImplTest {

   @Test
   void testGunCarriageRotateBackToParking_FromAngleGreaterThan180() {

      // Given
      double parkingAngle = 300.0;
      double expectedDegree = -90.0;

      Position gunCarriagePos = spy(Positions.of(90, 410).rotate(-60));
      GunCarriage gunCarriage = SimpleGunCarriageBuilder.builder()
            .withRotationSpeed(180)
            .withGun(mock(Gun.class))
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(5)
                  .withCenter(gunCarriagePos)
                  .build())
            .build();

      // When
      gunCarriage.turn2ParkPosition(parkingAngle);

      // Then
      assertThat(gunCarriage.getShape().getCenter().getDirection().getAngle(), is(parkingAngle));
      verify(gunCarriagePos).rotate(eq(expectedDegree));
   }

   @Test
   void testGunCarriageRotateBackToParking_FromAngleLessThan180AndGreaterZero() {

      // Given
      double parkingAngle = 180.0;
      double expectedDegree = 150.0;

      Position gunCarriagePos = spy(Positions.of(90, 410).rotate(-60));
      GunCarriage gunCarriage = SimpleGunCarriageBuilder.builder()
            .withRotationSpeed(180)
            .withGun(mock(Gun.class))
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(5)
                  .withCenter(gunCarriagePos)
                  .build())
            .build();

      // When
      gunCarriage.turn2ParkPosition(parkingAngle);

      // Then
      assertThat(gunCarriage.getShape().getCenter().getDirection().getAngle(), is(parkingAngle));
      verify(gunCarriagePos).rotate(eq(expectedDegree));
   }

   @Test
   void testGunCarriageRotateBackToParking_FromAngleSmallerThanMinu180() {

      // Given
      double parkingAngle = 0.0;
      double expectedDegree = 90.0;

      Position gunCarriagePos = spy(Positions.of(90, 410).rotate(-180));
      GunCarriage gunCarriage = SimpleGunCarriageBuilder.builder()
            .withRotationSpeed(180)
            .withGun(mock(Gun.class))
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(5)
                  .withCenter(gunCarriagePos)
                  .build())
            .build();

      // When
      gunCarriage.turn2ParkPosition(parkingAngle);

      // Then
      assertThat(gunCarriage.getShape().getCenter().getDirection().getAngle(), is(parkingAngle));
      verify(gunCarriagePos).rotate(eq(expectedDegree));
   }

   @Test
   void testGunCarriageTurn90DegreesRight() {

      // Given
      double degree = 90;
      double radius = 5.0;
      int height = 10;
      Position pos = Positions.of(radius, radius);
      Position expectedPos = Positions.of(radius, radius).rotate(degree);
      Position expectedGunPos = expectedPos.movePositionForward4Distance(radius + height / 2);
      GunCarriage gunCarriage = SimpleGunCarriageBuilder.builder()
            .withRotationSpeed(90)
            .withGun(DefaultGunBuilder.builder()
                  .withGunProjectileType(ProjectileTypes.BULLET)
                  .withGunShape(GunShapeBuilder.builder()
                        .withBarrel(RectangleBuilder.builder()
                              .withHeight(height)
                              .withWidth(height)
                              .withCenter(pos)
                              .withOrientation(Orientation.VERTICAL)
                              .build())
                        .build())
                  .withGunConfig(GunConfigBuilder.builder()
                        .withRoundsPerMinute(5)
                        .withSalveSize(1)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
                              .withVelocity(1)
                              .build())
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
