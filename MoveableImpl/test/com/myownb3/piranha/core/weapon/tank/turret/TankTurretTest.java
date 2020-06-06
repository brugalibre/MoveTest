package com.myownb3.piranha.core.weapon.tank.turret;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.tank.TankHolder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.turret.TankTurret.TankTurretBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretScanner;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

class TankTurretTest {

   @Test
   void testAutodetect_WithAdjust() {

      // Given
      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;

      TankHolder tankHolder = new TankHolder();

      Position tankPos = Positions.of(200, 200);
      Position turretPos = Positions.of(200, 200).rotate(180);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(TankTurretBuilder.builder()
                  .withParkingAngleEvaluator(() -> tankHolder.getPosition().getDirection().getAngle())
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(0)
                        .withDetectorAngle(0)
                        .withDetectorReach(0)
                        .withEvasionAngle(0)
                        .withEvasionDistance(0)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(360)// yes thats pretty fast
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
                                    .withHeight(gunHeight)
                                    .withWidth(gunWidth)
                                    .withCenter(turretPos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(turretPos)
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .withOrientation(Orientation.HORIZONTAL)
                  .build())
            .build();

      // when
      tankHolder.setAndReturnTank(tank);
      tank.autodetect();

      // then
      assertThat(turretPos.getDirection(), is(not(tank.getTurret().getShape().getCenter().getDirection())));
      assertThat(tank.getPosition().getDirection(), is(tank.getTurret().getShape().getCenter().getDirection()));
   }

   @Test
   void testAutodetect_WithoutAdjust_BecauseShooting() {

      // Given
      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;

      TankHolder tankHolder = new TankHolder();

      Position tankPos = Positions.of(200, 200);
      Position turretPos = Positions.of(200, 200).rotate(180);
      TurretScanner turretScanner = mock(TurretScanner.class);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(ShootingTankTurretBuilder.builder()
                  .withParkingAngleEvaluator(() -> tankHolder.getPosition().getDirection().getAngle())
                  .withTurretScanner(turretScanner)
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(0)
                        .withDetectorAngle(0)
                        .withDetectorReach(0)
                        .withEvasionAngle(0)
                        .withEvasionDistance(0)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(360)// yes thats pretty fast
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
                                    .withHeight(gunHeight)
                                    .withWidth(gunWidth)
                                    .withCenter(turretPos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(turretPos)
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .withOrientation(Orientation.HORIZONTAL)
                  .build())
            .build();

      // when
      tankHolder.setAndReturnTank(tank);
      tank.autodetect();

      // then
      assertThat(turretPos.getDirection(), is(tank.getTurret().getShape().getCenter().getDirection()));
   }

   @Test
   void testAutodetect_WithoutAdjust_BecauseAlreadyInParkingPos() {

      // Given
      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;

      TankHolder tankHolder = new TankHolder();

      Position tankPos = Positions.of(200, 200);
      Position turretPos = Positions.of(200, 200).rotate(180);
      TurretScanner turretScanner = mock(TurretScanner.class);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(TankTurretBuilder.builder()
                  .withParkingAngleEvaluator(() -> turretPos.getDirection().getAngle())
                  .withTurretScanner(turretScanner)
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(0)
                        .withDetectorAngle(0)
                        .withDetectorReach(0)
                        .withEvasionAngle(0)
                        .withEvasionDistance(0)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(360)// yes thats pretty fast
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
                                    .withHeight(gunHeight)
                                    .withWidth(gunWidth)
                                    .withCenter(turretPos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(turretPos)
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .withOrientation(Orientation.HORIZONTAL)
                  .build())
            .build();

      // when
      tankHolder.setAndReturnTank(tank);
      tank.autodetect();

      // then
      assertThat(turretPos.getDirection(), is(tank.getTurret().getShape().getCenter().getDirection()));
   }

   public static class ShootingTankTurretBuilder extends TankTurretBuilder {

      private ShootingTankTurretBuilder() {
         super();
      }

      public static ShootingTankTurretBuilder builder() {
         return new ShootingTankTurretBuilder();
      }

      @Override
      protected TurretBuilder getThis() {
         return this;
      }

      @Override
      public TankTurret build() {
         buildTurretShape();
         ShootingTankTurret tankTurret = new ShootingTankTurret(turretScanner, gunCarriage, turretShape, () -> 0.0);
         setTurretScanner(tankTurret);
         return tankTurret;
      }
   }

   private static class ShootingTankTurret extends TankTurret {

      protected ShootingTankTurret(TurretScanner turretScanner, GunCarriage gunCarriage, TurretShape turretShape,
            ParkingAngleEvaluator parkingAngleEvaluator) {
         super(turretScanner, gunCarriage, turretShape, parkingAngleEvaluator);
         this.state = TurretState.SHOOTING;
      }
   }
}
