package com.myownb3.piranha.core.weapon.turret;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.PositionTransformator;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
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
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.weapon.turret.strategy.handler.impl.human.HumanControlledTurretStrategyHandler;
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
   void testTurretImpl_WithHumanTurret() {

      // Given
      double radius = 5.0;
      Position gunCarriagePos = Positions.of(radius, radius);

      GunCarriage gunCarriage = spy(SimpleGunCarriageBuilder.builder()
            .withGun(BulletGunBuilder.builder()
                  .withGunConfig(GunConfigBuilder.builder()
                        .withRoundsPerMinute(1)
                        .withSalveSize(1)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                              .withVelocity(1)
                              .build())
                        .build())
                  .withGunShape(GunShapeBuilder.builder()
                        .withBarrel(RectangleBuilder.builder()
                              .withHeight(5)
                              .withWidth(2)
                              .withCenter(gunCarriagePos)
                              .withOrientation(Orientation.HORIZONTAL)
                              .build())
                        .build())
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(5)
                  .withCenter(gunCarriagePos)
                  .build())
            .build());
      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.emptyList())
            .withGunCarriage(gunCarriage)
            .withTurretStrategyHandler(new HumanControlledTurretStrategyHandler(gunCarriage))
            .build();

      // When
      turretImpl.autodetect();

      // Then
      verify(gunCarriage).aimTargetPos(eq(gunCarriagePos));
   }

   @Test
   void testPositionTransformator() {

      // Given
      Position turretPos = Positions.of(5, 5);
      Position transformedPosition = Positions.of(10, 10);
      PositionTransformator positionTransformator = pos -> transformedPosition;

      TurretImpl turretImpl = TurretBuilder.builder()
            .withGridElementEvaluator((position, distance) -> Collections.emptyList())
            .withDetector(mock(IDetector.class))
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .withPositionTransformator(positionTransformator)
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
                        .withRadius(5)
                        .withAmountOfPoints(5)
                        .withCenter(turretPos)
                        .build())
                  .build())
            .build();

      // When
      turretImpl.getShape().transform(turretPos);

      // Then
      assertThat(turretImpl.getShape().getCenter(), is(transformedPosition));
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
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
                        .withRadius(5)
                        .withAmountOfPoints(5)
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
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
                        .withRadius(5)
                        .withAmountOfPoints(5)
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
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
                        .withRadius(5)
                        .withAmountOfPoints(5)
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
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
            .withDetector(mock(IDetector.class))
            .withGunCarriage(SimpleGunCarriageBuilder.builder()
                  .withGun(BulletGunBuilder.builder()
                        .withGunConfig(GunConfigBuilder.builder()
                              .withRoundsPerMinute(1)
                              .withSalveSize(1)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
      Position targetGridElemPos = Positions.of(9, 10).movePositionForward4Distance(height * 2);

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorAngle(45)
            .withDetectorReach(30)
            .withEvasionAngle(0)
            .withEvasionDistance(0)
            .build();

      int turnIncrement = 4;

      Obstacle simpleGridElement = ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(targetGridElemPos)
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
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
      TurretState stateAfterFirstDetect = turretImpl.turretStrategyHandler.getTurretStatus();
      turretImpl.autodetect();

      // Then
      assertThat(stateAfterFirstDetect, is(TurretState.TARGET_DETECTED));
      assertThat(turretImpl.turretStrategyHandler.getTurretStatus(), is(TurretState.ACQUIRING));
   }

   @Test
   void testTurretImpl_DetectAndTurn() {
      // Given
      double radius = 5.0;
      int height = 10;
      Position pos = Positions.of(radius, radius);
      Position targetGridElemPos = Positions.of(9, 10).movePositionForward4Distance(height * 2);

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorAngle(45)
            .withDetectorReach(30)
            .withEvasionAngle(0)
            .withEvasionDistance(0)
            .build();

      int turnIncrement = 4;

      Obstacle simpleGridElement = ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(targetGridElemPos)
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
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                                    .withVelocity(1)
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
      boolean isAcquring3rdRound = turretImpl.isAcquiring();
      turretImpl.autodetect();// 5. continue acquiring -> now we reached the other angle
      boolean isAcquiring4thRound = turretImpl.isAcquiring();
      boolean isShooting4thRound = turretImpl.isShooting();
      turretImpl.autodetect();// 6. we don't turn again here, since we are done
      boolean isShooting5thRound = turretImpl.isShooting();

      // Then
      assertThat(isAcquring3rdRound, is(isAcquring3rdRound));
      assertThat(isAcquiring4thRound, is(false));
      assertThat(isShooting4thRound, is(true));
      assertThat(isShooting5thRound, is(true));
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
            .withShape(CircleBuilder.builder()
                  .withRadius((int) obstacleRadius)
                  .withAmountOfPoints(5)
                  .withCenter(targetGridElemPos)
                  .build())
            .withVelocity(500)
            .build();

      SimpleGunCarriageImpl gunCarriage = SimpleGunCarriageBuilder.builder()
            .withRotationSpeed(turnIncrement)
            .withGun(BulletGunBuilder.builder()
                  .withGunConfig(GunConfigBuilder.builder()
                        .withRoundsPerMinute(1)
                        .withSalveSize(1)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(0))
                              .withVelocity(1)
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
            .withGunCarriage(gunCarriage)
            .withTargetPositionLeadEvaluator(new TestTargetPositionLeadEvaluator())
            .build();

      // When
      turretImpl.autodetect();// 1. Detect the target-GridElement
      turretImpl.autodetect();// 2. Detect the target-Position (with or without lead)
      turretImpl.autodetect();// 4. continue
      turretImpl.autodetect();// 5. continue
      turretImpl.autodetect();// 6. continue acquiring -> now we reached the other angle

      assertThat(gunCarriage.getShape().getCenter().getDirection(), is(not(pos.getDirection())));

      simpleGridElement.moveForward();

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
