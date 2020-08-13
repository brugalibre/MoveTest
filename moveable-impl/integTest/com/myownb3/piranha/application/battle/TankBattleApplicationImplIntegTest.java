package com.myownb3.piranha.application.battle;

import static com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
import static com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationBuilder;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationTankBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTankTurretBuilder;
import com.myownb3.piranha.application.battle.util.MoveableAdder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;

class TankBattleApplicationImplIntegTest {

   @Test
   void testBuildTankBattleApplication_WithTankWithTurretImpl() {

      // Given
      Position tankPos = Positions.of(550, 100).rotate(180);
      Position tankTurretPos = Positions.of(410, 100).rotate(180);
      Position expectedEndTankPos = Positions.of(540.6068461997, 299.2030306712).rotate(-85.2);
      List<EndPosition> endPositions = new ArrayList<>();
      endPositions.add(EndPositions.of(Positions.of(440, 100), 10));
      endPositions.add(EndPositions.of(Positions.of(440, 300), 10));
      EndPosition lastEndPos = EndPositions.of(Positions.of(550, 300), 10);
      endPositions.add(lastEndPos);

      Grid grid = GridBuilder.builder()
            .withMaxX(800)
            .withMaxY(900)
            .withMinX(0)
            .withMinY(0)
            .build();

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(250)
            .withDetectorAngle(360)
            .build();

      DetectorConfig missileDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(200)
            .withDetectorAngle(120)
            .build();

      TankHolder tankHolder = new TankHolder();
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(mock(MoveableAdder.class))
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .build())
            .addTankGridElement(tankHolder, TankBattleApplicationTankBuilder.builder()
                  .withEndPositions(endPositions)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withGrid(grid)
                  .withTankHeight(80)
                  .withTankTurretHeight(DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM)
                  .withTankPos(tankPos)
                  .withTankWidth(40)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE)
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(10)
                        .withReturningMinDistance(1)
                        .withReturningAngleMargin(1)
                        .withPassingDistance(25)
                        .withPostEvasionReturnAngle(4)
                        .withDetectorConfig(DetectorConfigBuilder.builder()
                              .build())
                        .build())
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                        .withParkingAngleEvaluator(() -> tankHolder.getPosition().getDirection().getAngle())
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.MISSILE)
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.MISSILE_SHOT_SOUND)
                                    .build())
                              .withSalveSize(1)
                              .withRoundsPerMinute(100)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.builder()
                                          .withDimensionRadius(3)
                                          .withHeightFromBottom(DEFAULT_TANK_HEIGHT_FROM_BOTTOM + DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM)
                                          .build())
                                    .withVelocity(10)
                                    .withTargetGridElementEvaluator(TargetGridElementEvaluatorBuilder.builder()
                                          .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                                          .withDetector(DetectorBuilder.builder()
                                                .withDetectorAngle(missileDetectorConfig.getDetectorAngle())
                                                .withDetectorReach(missileDetectorConfig.getDetectorReach())
                                                .build())
                                          .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                                          .build())
                                    .withProjectileDamage(300)
                                    .build())
                              .build())
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(15)
                              .withAmountOfPoints(15)
                              .withCenter(tankTurretPos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(15)
                        .withGunWidth(10)
                        .withTurretPosition(tankTurretPos)
                        .withTurretRotationSpeed(6)
                        .build())
                  .build(tankHolder))
            .build();
      TankGridElement tankGridElement = tankBattleApplication.getTankGridElements().get(0);

      // When
      while (!lastEndPos.checkIfHasReached(tankGridElement)) {
         tankBattleApplication.run();
      }

      // Then
      assertThat(tankGridElement.getPosition(), is(expectedEndTankPos));
   }
}
