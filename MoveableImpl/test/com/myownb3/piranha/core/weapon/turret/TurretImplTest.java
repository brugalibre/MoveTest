package com.myownb3.piranha.core.weapon.turret;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.StroomTrooper;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TargetGridElement;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TurretScanner;

class TurretImplTest {

   @BeforeEach
   public void setUp() {
      ProjectileFactory.INSTANCE.registerGrid(mock(Grid.class));
   }

   @AfterEach
   public void tearDown() {
      ProjectileFactory.INSTANCE.deregisterGrid();
   }

   @Test
   void testCustomWithBelligerent() {

      // Given
      Position turretPos = Positions.of(5, 5);
      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.emptyList())
            .withDetector(mock(IDetector.class))
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(5)
                                    .withWidth(2)
                                    .withCenter(turretPos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius((int) 5)
                        .withAmountOfPoints((int) 5)
                        .withCenter(turretPos)
                        .build())
                  .build())
            .build();

      // When
      BelligerentParty actualBelligerentParty = turretImpl.getBelligerentParty();

      // Then
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.GALACTIC_EMPIRE));
   }

   @Test
   void testDefaultWithBelligerent() {

      Position turretPos = Positions.of(5, 5);
      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.emptyList())
            .withDetector(mock(IDetector.class))
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(5)
                                    .withWidth(2)
                                    .withCenter(turretPos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius((int) 5)
                        .withAmountOfPoints((int) 5)
                        .withCenter(turretPos)
                        .build())
                  .build())
            .build();

      // When
      BelligerentParty actualBelligerentParty = turretImpl.getBelligerentParty();

      // Then
      assertThat(actualBelligerentParty, is(BelligerentPartyConst.REBEL_ALLIANCE));
   }

   @Test
   void testDefaultWithBelligerent_IsEmpireEnemy() {

      Position turretPos = Positions.of(5, 5);
      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.emptyList())
            .withDetector(mock(IDetector.class))
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(5)
                                    .withWidth(2)
                                    .withCenter(turretPos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius((int) 5)
                        .withAmountOfPoints((int) 5)
                        .withCenter(turretPos)
                        .build())
                  .build())
            .build();

      // When
      boolean isActualEnemy = turretImpl.isEnemy(new StroomTrooper());

      // Then
      assertThat(isActualEnemy, is(true));
   }

   @Test
   void testTurretImpl_RearmostAndForeMostPos() {

      // Given
      double radius = 6.0;
      int height = 10;
      Position anyPos = Positions.of(Math.random() * 5, Math.random() * 5);
      Position pos = Positions.of(5, 5);
      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.emptyList())
            .withDetector(mock(IDetector.class))
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(height)
                                    .withWidth(2)
                                    .withCenter(anyPos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius((int) radius)
                        .withAmountOfPoints((int) radius)
                        .withCenter(pos)
                        .build())
                  .build())
            .build();

      // When
      TurretShape turretShape = turretImpl.getShape();
      Position rearmostPosition = turretShape.getRearmostPosition();
      Position foremostPosition = turretShape.getForemostPosition();

      // Then
      assertThat(rearmostPosition, is(Positions.of(5, -1)));
      assertThat(foremostPosition, is(Positions.of(5, 21)));
   }

   @Test
   void testTurretImpl_DetectAndScan() {

      // Given
      double radius = 5.0;
      int height = 10;
      Position pos = Positions.of(radius, radius);

      TurretScanner turretScanner = mock(TurretScanner.class);
      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.emptyList())
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(height)
                                    .withWidth(2)
                                    .withCenter(pos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius((int) radius)
                        .withAmountOfPoints((int) radius)
                        .withCenter(pos)
                        .build())
                  .build())
            .withTurretScanner(turretScanner)
            .build();

      // When
      turretImpl.autodetect();

      // Then
      verify(turretScanner).scan(eq(TurretState.SCANNING));
   }

   @Test
   void testTurretImpl_DetectTwiceReachStateTargetDetecting() {

      // Given
      double radius = 5.0;
      int height = 10;
      Position pos = Positions.of(radius, radius);
      Position targetGridElemPos = Positions.movePositionForward4Distance(Positions.of(9, 10), height * 2);

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorAngle(45)
            .withDetectorReach(30)
            .withEvasionAngle(0)
            .withEvasionDistance(0)
            .build();

      int turnIncrement = 4;

      Obstacle simpleGridElement = ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(targetGridElemPos)
            .build();

      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.singletonList(simpleGridElement))
            .withDetector(DetectorBuilder.builder()
                  .withAngleInc(detectorConfig.getEvasionAngleInc())
                  .withDetectorAngle(detectorConfig.getDetectorAngle())
                  .withDetectorReach(detectorConfig.getDetectorReach())
                  .withEvasionAngle(detectorConfig.getDetectorAngle())
                  .withEvasionDistance(detectorConfig.getEvasionDistance())
                  .build())
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withRotationSpeed(turnIncrement)
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(height)
                                    .withWidth(2)
                                    .withCenter(pos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius((int) radius)
                        .withAmountOfPoints((int) radius)
                        .withCenter(pos)
                        .build())
                  .build())
            .withTargetPositionLeadEvaluator(new TestTargetPositionLeadEvaluator())
            .build();

      // When
      turretImpl.autodetect();
      TurretState stateAfterFirstDetect = turretImpl.state;
      turretImpl.autodetect();

      // Then
      assertThat(stateAfterFirstDetect, is(TurretState.TARGET_DETECTED));
      assertThat(turretImpl.state, is(TurretState.ACQUIRING));
   }

   @Test
   void testTurretImpl_DetectAndTurn() {
      // Given
      double radius = 5.0;
      int height = 10;
      Position pos = Positions.of(radius, radius);
      Position targetGridElemPos = Positions.movePositionForward4Distance(Positions.of(9, 10), height * 2);

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorAngle(45)
            .withDetectorReach(30)
            .withEvasionAngle(0)
            .withEvasionDistance(0)
            .build();

      int turnIncrement = 4;

      Obstacle simpleGridElement = ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(targetGridElemPos)
            .build();

      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.singletonList(simpleGridElement))
            .withDetector(DetectorBuilder.builder()
                  .withAngleInc(detectorConfig.getEvasionAngleInc())
                  .withDetectorAngle(detectorConfig.getDetectorAngle())
                  .withDetectorReach(detectorConfig.getDetectorReach())
                  .withEvasionAngle(detectorConfig.getDetectorAngle())
                  .withEvasionDistance(detectorConfig.getEvasionDistance())
                  .build())
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withRotationSpeed(turnIncrement)
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(height)
                                    .withWidth(2)
                                    .withCenter(pos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius((int) radius)
                        .withAmountOfPoints((int) radius)
                        .withCenter(pos)
                        .build())
                  .build())
            .withTargetPositionLeadEvaluator(new TestTargetPositionLeadEvaluator())
            .build();

      // When
      turretImpl.autodetect();// 1. Detect the target-GridElement
      turretImpl.autodetect();// 2. Detect the target-Position (with or without lead)
      turretImpl.autodetect();// 3. continue
      turretImpl.autodetect();// 4. continue
      TurretState stateStatus3rdRound = turretImpl.getTurretStatus();
      turretImpl.autodetect();// 5. continue acquiring -> now we reached the other angle
      TurretState stateStatus4thRound = turretImpl.getTurretStatus();
      turretImpl.autodetect();// 6. we don't turn again here, since we are done
      TurretState stateStatus5thRound = turretImpl.getTurretStatus();

      // Then
      assertThat(stateStatus3rdRound, is(TurretState.ACQUIRING));
      assertThat(stateStatus4thRound, is(TurretState.SHOOTING));
      assertThat(stateStatus5thRound, is(TurretState.SHOOTING));
      assertThat(turretImpl.isShooting(), is(true));
   }

   @Test
   void testTurretImpl_TurnBackToInitial() {
      // Given
      double obstacleRadius = 5.0;
      int turnIncrement = 4;
      int turretRadius = 1;
      int height = 2;
      Position pos = Positions.of(obstacleRadius, obstacleRadius);
      Position targetGridElemPos = Positions.of(6, 10);

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorAngle(45)
            .withDetectorReach(30)
            .withEvasionAngle(0)
            .withEvasionDistance(0)
            .build();

      Moveable simpleGridElement = MoveableObstacleBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(500)
                  .withMaxY(500)
                  .build())
            .withPosition(targetGridElemPos)
            .withShape(CircleBuilder.builder()
                  .withRadius((int) obstacleRadius)
                  .withAmountOfPoints(5)
                  .withCenter(targetGridElemPos)
                  .build())
            .build();

      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.singletonList(simpleGridElement))
            .withDetector(DetectorBuilder.builder()
                  .withAngleInc(detectorConfig.getEvasionAngleInc())
                  .withDetectorAngle(detectorConfig.getDetectorAngle())
                  .withDetectorReach(detectorConfig.getDetectorReach())
                  .withEvasionAngle(detectorConfig.getDetectorAngle())
                  .withEvasionDistance(detectorConfig.getEvasionDistance())
                  .build())
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withRotationSpeed(turnIncrement)
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withVelocity(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimension(new DimensionImpl(0, 0, 0, 0))
                                    .build())
                              .build())
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(height)
                                    .withWidth(2)
                                    .withCenter(pos)
                                    .withOrientation(Orientation.HORIZONTAL)
                                    .build())
                              .build())
                        .build())
                  .withShape(CircleBuilder.builder()
                        .withRadius(turretRadius)
                        .withAmountOfPoints(5)
                        .withCenter(pos)
                        .build())
                  .build())
            .withTargetPositionLeadEvaluator(new TestTargetPositionLeadEvaluator())
            .build();

      // When
      turretImpl.autodetect();// 1. Detect the target-GridElement
      turretImpl.autodetect();// 2. Detect the target-Position (with or without lead)
      turretImpl.autodetect();// 4. continue
      turretImpl.autodetect();// 5. continue
      turretImpl.autodetect();// 6. continue acquiring -> now we reached the other angle

      GunCarriage gunCarriage = turretImpl.getGunCarriage();
      assertThat(gunCarriage.getShape().getCenter().getDirection(), is(not(pos.getDirection())));

      simpleGridElement.moveForward(500);

      turretImpl.autodetect();// 5. return back
      turretImpl.autodetect();// 6. return back
      turretImpl.autodetect();// 7. return back
      turretImpl.autodetect();// 8. return back
      turretImpl.autodetect();// 9. return back
      turretImpl.autodetect();// 10. return back

      // Then
      assertThat(turretImpl.getShape().getCenter().getDirection(), is(pos.getDirection()));
   }

   private final class TestTargetPositionLeadEvaluator implements TargetPositionLeadEvaluator {
      @Override
      public Position calculateTargetConsideringLead(TargetGridElement targetGridElement, Position turretPos) {
         return targetGridElement.getCurrentGridElementPosition();
      }
   }

}
