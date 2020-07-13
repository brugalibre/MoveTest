package com.myownb3.piranha.core.weapon.tank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.StroomTrooper;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShapeImpl;

class TankImplTest {

   @Test
   void testDestroyTank() {

      // Given
      double health = 4;
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTankDetector(mock(TankDetector.class))
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(1)
                        .withDetectorAngle(1)
                        .withDetectorReach(1)
                        .withEvasionAngle(1)
                        .withEvasionDistance(1)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(3)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.BULLET)
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(350)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                                          .withVelocity(3)
                                          .build())
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(5)
                                          .withWidth(5)
                                          .withCenter(Positions.of(5, 5))
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(5)
                              .withAmountOfPoints(5)
                              .withCenter(Positions.of(5, 5))
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(30)
                  .withWidth(30)
                  .build())
            .withHealth(health)
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      ProjectileGridElement projectile = mock(ProjectileGridElement.class);
      when(projectile.getDamage()).thenReturn(DamageImpl.of(health + 1));

      // When
      tank.onCollision(Collections.singletonList(projectile));
      boolean actualIsDestroyed = tank.isDestroyed();

      // Then
      assertThat(actualIsDestroyed, is(true));
   }


   @Test
   void testThatTurretHasSamePartyThanTank() {

      // Given
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;
      double gunHeight = 10;
      double gunWidth = 4;

      // When
      Grid grid = mock(Grid.class);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTankDetector(TankDetectorBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(1)
                        .withDetectorAngle(1)
                        .withDetectorReach(1)
                        .withEvasionAngle(1)
                        .withEvasionDistance(1)
                        .build())
                  .withTankGridElement(() -> null)
                  .build())
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(1)
                        .withDetectorAngle(1)
                        .withDetectorReach(1)
                        .withEvasionAngle(1)
                        .withEvasionDistance(1)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(3)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.BULLET)
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(350)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                                          .withVelocity(3)
                                          .build())
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunHeight)
                                          .withWidth(gunWidth)
                                          .withCenter(turretPos)
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(5)
                              .withAmountOfPoints(5)
                              .withCenter(turretPos)
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(turretPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // Then
      assertThat(tank.getTurret().getShape().getCenter(), is(tank.getPosition()));
      assertThat(tank.getShape() instanceof TankShape, is(true));
      assertThat(tank.getBelligerentParty(), is(tank.getTurret().getBelligerentParty()));
   }

   @Test
   void testCustomWithBelligerent() {

      // Given
      Position turretPos = Positions.of(5, 5);
      Turret turret = mock(Turret.class);

      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(turretPos);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(turret)
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withHull(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      BelligerentParty actualBelligerentParty = tank.getBelligerentParty();

      // Then
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.GALACTIC_EMPIRE));
   }

   @Test
   void testDefaultWithBelligerent() {

      Position turretPos = Positions.of(5, 5);
      Turret turret = mock(Turret.class);

      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(turretPos);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(turret)
            .withHull(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      BelligerentParty actualBelligerentParty = tank.getBelligerentParty();

      // Then
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.REBEL_ALLIANCE));
   }

   @Test
   void testDefaultWithBelligerent_IsEmpireEnemy() {

      Position turretPos = Positions.of(5, 5);
      Turret turret = mock(Turret.class);

      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(turretPos);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(turret)
            .withHull(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      boolean isActualEnemy = tank.isEnemy(new StroomTrooper());

      // Then
      assertThat(isActualEnemy, is(true));
   }

   @Test
   void testAlwaysMoveForward() {

      // Given
      Position tankPos = Positions.of(10, 10).rotate(-45);
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;

      Turret turret = mock(Turret.class);

      when(turret.isShooting()).thenReturn(true);
      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(turretPos);

      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTankDetector(mock(TankDetector.class))
            .withTurret(turret)
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.ALWAYS_MOVE_AND_SHOOT)
            .build();

      // When
      tank.autodetect();

      // Then
      verify(tank.getTankEngine()).moveForward();
      verify(turret).autodetect();
   }

   @Test
   void testMoveForwardWhenNotShooting() {

      // Given
      Position tankPos = Positions.of(10, 10).rotate(-45);
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;

      Turret turret = mock(Turret.class);

      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(turretPos);

      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTankDetector(mock(TankDetector.class))
            .withTurret(turret)
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      tank.autodetect();

      // Then
      verify(tank.getTankEngine()).moveForward();
   }

   @Test
   void testStopWhenShooting() {

      // Given
      Position tankPos = Positions.of(10, 10).rotate(-45);
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;

      Turret turret = mockShootingTurret(turretPos);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTankDetector(mock(TankDetector.class))
            .withTurret(turret)
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      tank.autodetect();

      // Then
      verify(tank.getTankEngine(), never()).moveForward();
   }

   @Test
   void testMoveWhenUnderFireAndShooting() {

      // Given
      Position tankPos = Positions.of(10, 10).rotate(-45);
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;

      Turret turret = mockShootingTurret(turretPos);
      TankDetector detector = mock(TankDetector.class);
      when(detector.isUnderFire()).thenReturn(true);
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTankDetector(detector)
            .withTurret(turret)
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      tank.autodetect();

      // Then
      verify(tank.getTankEngine()).moveForward();
   }

   private static Turret mockShootingTurret(Position turretPos) {
      Turret turret = mock(Turret.class);
      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.isShooting()).thenReturn(true);
      when(turret.getShape().getCenter()).thenReturn(turretPos);
      return turret;
   }

   @Test
   void testAutodetectTank() {

      // Given
      Position tankPos = Positions.of(10, 10).rotate(-45);
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;

      Turret turret = mock(Turret.class);
      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(turretPos);

      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTankDetector(mock(TankDetector.class))
            .withTurret(turret)
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      tank.autodetect();

      // Then
      verify(turret).autodetect();
   }

   @Test
   void testBuildTank() {

      // Given
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;
      double gunHeight = 10;
      double gunWidth = 4;

      // When
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(1)
                        .withDetectorAngle(1)
                        .withDetectorReach(1)
                        .withEvasionAngle(1)
                        .withEvasionDistance(1)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(3)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.BULLET)
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(350)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                                          .withVelocity(3)
                                          .build())
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunHeight)
                                          .withWidth(gunWidth)
                                          .withCenter(turretPos)
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(5)
                              .withAmountOfPoints(5)
                              .withCenter(turretPos)
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(turretPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // Then
      assertThat(tank.getTurret().getShape().getCenter(), is(tank.getPosition()));
      assertThat(tank.getShape() instanceof TankShape, is(true));
   }

   @Test
   void testReSetTankPosition() {

      // Given
      Position newTankPos = Positions.of(65, 43).rotate(465);
      Position turretPos = Positions.of(5, 5);
      int tankWidth = 10;
      int tankHeight = 30;
      double gunHeight = 10;
      double gunWidth = 4;
      Tank tank = TankBuilder.builder()
            .withTankEngine(mock(TankEngine.class))
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(1)
                        .withDetectorAngle(1)
                        .withDetectorReach(1)
                        .withEvasionAngle(1)
                        .withEvasionDistance(1)
                        .build())
                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(3)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.BULLET)
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(350)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                                          .withVelocity(3)
                                          .build())
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunHeight)
                                          .withWidth(gunWidth)
                                          .withCenter(turretPos)
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(5)
                              .withAmountOfPoints(5)
                              .withCenter(turretPos)
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(turretPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .build())
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      // When
      tank.getShape().transform(newTankPos);
      tank.getShape().transform(newTankPos);

      // Then
      Position actualTurretPos = tank.getTurret().getShape().getCenter();

      assertThat(tank.getPosition(), is(newTankPos));
      assertThat(actualTurretPos.getX(), is(tank.getPosition().getX()));
      assertThat(actualTurretPos.getY(), is(tank.getPosition().getY()));
      assertThat(actualTurretPos.getDirection(), is(not(tank.getPosition().getDirection())));
   }
}
