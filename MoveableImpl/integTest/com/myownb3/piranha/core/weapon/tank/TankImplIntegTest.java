
package com.myownb3.piranha.core.weapon.tank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;

class TankImplIntegTest {

   @Test
   void testBuildDefaultTankEngine() {

      // Given
      Position tankPos = Positions.of(10, 10);
      Position turretPos = Positions.of(50, 50);
      int tankWidth = 10;
      int tankHeight = 30;
      EndPosition endPos = EndPositions.of(50, 50.1);
      Position expectedPosition = Positions.of(10, 10.2);
      List<EndPosition> endPositions = Collections.singletonList(endPos);

      Tank tank = TankBuilder.builder()
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(0)
                        .withDetectorAngle(0)
                        .withDetectorReach(0)
                        .withEvasionAngle(0)
                        .withEvasionDistance(0)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(3)
                        .withGun(BulletGunBuilder.builder()
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(350)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimension(new DimensionImpl(0, 0, 3, 3))
                                          .build())
                                    .withVelocity(3)
                                    .build())
                              .withRectangle(RectangleBuilder.builder()
                                    .withHeight(10)
                                    .withWidth(5)
                                    .withCenter(turretPos)
                                    .withOrientation(Orientation.VERTICAL)
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(5)
                              .withAmountOfPoints(5)
                              .withCenter(turretPos)
                              .build())
                        .build())
                  .build())
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMaxY(50)
                  .build())
            .withEngineVelocity(2)
            .withEndPositions(endPositions)
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .build();

      // When
      tank.autodetect();

      // Then
      assertThat(tank.getPosition(), is(expectedPosition));
   }
}
