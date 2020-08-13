package com.myownb3.piranha.application.battle.impl;

import static com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationBuilder;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationTankBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationHumanTurretBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTankTurretBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTurretBuilder;
import com.myownb3.piranha.application.battle.util.MoveableAdder;
import com.myownb3.piranha.application.battle.util.MoveableAdder.MoveableAdderBuilder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl.DefaultGunCarriageBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.battle.weapon.tank.engine.human.HumanTankEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.TurretCluster;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.human.HumanControlledTurretStrategyHandler;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.lazy.LazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;

class TankBattleApplicationImplTest {

   @Test
   void testRunTankBattleApplication_CycleNotOver_GridElementIsAlive() {

      // Given
      TankBattleApplicationRunnerTestCaseBuilder tcb = new TankBattleApplicationRunnerTestCaseBuilder()
            .withGrid(MirrorGridBuilder.builder()
                  .withMaxX(800)
                  .withMaxY(900)
                  .withMinX(0)
                  .withMinY(0)
                  .build())
            .withMoveableAdder(false)
            .addAutodetectableGridElement(mockTankGridElement(false))
            .addAutodetectableGridElement(mockObstacle())
            .build();

      // When
      tcb.tankBattleApplication.run();

      // Then
      verify(tcb.moveableAdder, never()).check4NewMoveables2Add(eq(tcb.grid), any());
      tcb.autodetectables.stream()
            .filter(AutoDetectable.class::isInstance)
            .map(AutoDetectable.class::cast)
            .forEach(autoDetectable -> verify(autoDetectable).autodetect());
   }

   @Test
   void testRunTankBattleApplication_CycleNotOver_GridElementIsDead() {

      // Given
      TankBattleApplicationRunnerTestCaseBuilder tcb = new TankBattleApplicationRunnerTestCaseBuilder()
            .withGrid(MirrorGridBuilder.builder()
                  .withMaxX(800)
                  .withMaxY(900)
                  .withMinX(0)
                  .withMinY(0)
                  .build())
            .withMoveableAdder(false)
            .addAutodetectableGridElement(mockTankGridElement(true))
            .build();

      // When
      tcb.tankBattleApplication.run();

      // Then
      verify(tcb.moveableAdder, never()).check4NewMoveables2Add(eq(tcb.grid), any());
      tcb.autodetectables.stream()
            .filter(AutoDetectable.class::isInstance)
            .map(AutoDetectable.class::cast)
            .forEach(autoDetectable -> verify(autoDetectable, never()).autodetect());
   }

   @Test
   void testRunTankBattleApplication_CycleOver() {

      // Given
      TankBattleApplicationRunnerTestCaseBuilder tcb = new TankBattleApplicationRunnerTestCaseBuilder()
            .withGrid(MirrorGridBuilder.builder()
                  .withMaxX(800)
                  .withMaxY(900)
                  .withMinX(0)
                  .withMinY(0)
                  .build())
            .withMoveableAdder(true)
            .build();

      // When
      tcb.tankBattleApplication.run();

      // Then
      verify(tcb.moveableAdder).check4NewMoveables2Add(eq(tcb.grid), any());
   }

   private GridElement mockObstacle() {
      return mockGridElement(Obstacle.class);
   }

   private TankGridElement mockTankGridElement(boolean isDestroyed) {
      TankGridElement tankGridElement = mockGridElement(TankGridElement.class);
      when(tankGridElement.isDestroyed()).thenReturn(isDestroyed);
      return tankGridElement;
   }

   private <T extends GridElement> T mockGridElement(Class<T> gEClass) {
      T gridElement = mock(gEClass);
      when(gridElement.getPosition()).thenReturn(Positions.of(5, 5));
      return gridElement;
   }

   private static class TankBattleApplicationRunnerTestCaseBuilder {

      private MoveableAdder moveableAdder;
      private Grid grid;
      private TankBattleApplication tankBattleApplication;
      private List<GridElement> autodetectables;

      private TankBattleApplicationRunnerTestCaseBuilder() {
         autodetectables = new ArrayList<>();
      }

      private TankBattleApplicationRunnerTestCaseBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      private TankBattleApplicationRunnerTestCaseBuilder addAutodetectableGridElement(GridElement gridElement) {
         this.autodetectables.add(gridElement);
         return this;
      }

      private TankBattleApplicationRunnerTestCaseBuilder withMoveableAdder(boolean isCycleDone) {
         this.moveableAdder = mock(MoveableAdder.class);
         when(moveableAdder.isCycleOver(anyInt())).thenReturn(isCycleDone);
         return this;
      }

      private TankBattleApplicationRunnerTestCaseBuilder build() {
         for (GridElement gridElement : autodetectables) {
            grid.addElement(gridElement);
         }
         this.tankBattleApplication = TankBattleApplicationBuilder.builder()
               .withGrid(grid)
               .withMoveableAdder(moveableAdder)
               .build();
         return this;
      }
   }

   @Test
   void testBuildTankBattleApplication_WithTankWithTurretImpl() {

      // Given
      double tankTurretHeight = GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
      double tankHeightFromGround = GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
      Position tankPos = Positions.of(550, 100).rotate(180);
      Position tankTurretPos = Positions.of(410, 100).rotate(180);

      List<EndPosition> endPositions = Collections.singletonList(EndPositions.of(Positions.of(440, 100), 10));

      MirrorGrid grid = MirrorGridBuilder.builder()
            .withMaxX(800)
            .withMaxY(900)
            .withMinX(0)
            .withMinY(0)
            .build();

      double turretRotationSpeed = 6;
      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;
      int projectileVelocity = 50;
      int engineVelocity = 12;

      // When
      TankHolder tankHolder = new TankHolder();
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(MoveableAdderBuilder.builder()
                  .withMoveableVelocity(8)
                  .withCounter(200)
                  .withPadding(0)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .build())
            .addTankGridElement(tankHolder, TankBattleApplicationTankBuilder.builder()
                  .withEndPositions(endPositions)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withGrid(grid)
                  .withTankHeight(tankHeight)
                  .withEngineVelocity(engineVelocity)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankPos(tankPos)
                  .withTankWidth(tankWidth)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE)
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(1)
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
                        .withDetectorConfig(mock(DetectorConfig.class))
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
                                          .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                          .build())
                                    .withVelocity(projectileVelocity)
                                    .withTargetGridElementEvaluator(TargetGridElementEvaluatorBuilder.builder()
                                          .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                                          .withDetector(DetectorBuilder.builder()
                                                .withDetectorAngle(1)
                                                .withDetectorReach(1)
                                                .build())
                                          .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                                          .build())
                                    .withProjectileDamage(300)
                                    .build())
                              .build())
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(tankTurretPos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(gunHeight)
                        .withGunWidth(gunWidth)
                        .withTurretPosition(tankTurretPos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .build(tankHolder))
            .build();

      // Then
      List<TankGridElement> turretGridElements = tankBattleApplication.getTankGridElements();
      assertThat(tankBattleApplication.getTurretGridElements().isEmpty(), is(true));
      assertThat(turretGridElements.size(), is(1));
      TankGridElement tankGridElement = turretGridElements.get(0);
      assertThat(tankGridElement.getPosition(), is(tankPos));
      assertThat(tankGridElement.getVelocity(), is(engineVelocity));
      assertThat(tankGridElement.getBelligerentParty(), is(BelligerentPartyConst.GALACTIC_EMPIRE));
      assertThat(TurretCluster.class.isAssignableFrom(tankGridElement.getTurret().getClass()), is(false));
      assertMuzzleBrake(tankGridElement.getTurret(), true);
   }

   @Test
   void testBuildTankBattleApplication_WithTankAndTurretClusterImpl() {

      // Given
      double tankTurretHeight = GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
      double tankHeightFromGround = GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
      MirrorGrid grid = MirrorGridBuilder.builder()
            .withMaxX(800)
            .withMaxY(900)
            .withMinX(0)
            .withMinY(0)
            .build();

      double turretRotationSpeed = 6;
      int tankWidth = 40;
      int tankHeight = 90;
      int battleTankGunCarriageRadius = 8;
      double battleTankGunHeight = 20;
      double battleTankGunWidth = 5;
      int projectileVelocity = 50;

      int battleShipParkingAngle = -90;
      TankHolder battleShipHolder = new TankHolder();
      Position battleShipPos = Positions.of(150, 600, tankHeightFromGround).rotate(90);
      Position tankTurret1Pos = Positions.of(140, 100).rotate(180);
      Position tankTurret2Pos = Positions.of(30, 100).rotate(180);

      List<EndPosition> battleShipEndPositions = new ArrayList<>();
      battleShipEndPositions.add(EndPositions.of(Positions.of(750, 600), 10));
      battleShipEndPositions.add(EndPositions.of(battleShipPos, 10));

      // When
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(MoveableAdderBuilder.builder()
                  .withMoveableVelocity(8)
                  .withCounter(200)
                  .withPadding(0)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .build())
            .addTankGridElement(battleShipHolder, TankBattleApplicationTankBuilder.builder()
                  .withEndPositions(battleShipEndPositions)
                  .withGrid(grid)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withTankPos(battleShipPos)
                  .withTankHeight(tankHeight * 2d)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankWidth(2 * tankWidth / 3d)
                  .withTankStrategy(TankStrategy.ALWAYS_MOVE_AND_SHOOT)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE_VAR2)
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(180)
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
                        .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                        .withPositionTransformator(transformedTankPos -> transformedTankPos.movePositionBackward4Distance(70))
                        .withDetectorConfig(DetectorConfigBuilder.builder()
                              .withDetectorReach(250)
                              .withDetectorAngle(360)
                              .build())
                        .withProjectileType(ProjectileTypes.LASER_BEAM)
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                                    .build())
                              .withSalveSize(2)
                              .withRoundsPerMinute(150)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.builder()
                                          .withDimensionRadius(2)
                                          .withHeightFromBottom(tankTurretHeight)
                                          .build())
                                    .withVelocity(projectileVelocity)
                                    .build())
                              .build())
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(battleTankGunCarriageRadius)
                              .withAmountOfPoints(battleTankGunCarriageRadius)
                              .withCenter(tankTurret1Pos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(battleTankGunHeight)
                        .withGunWidth(battleTankGunWidth)
                        .withTurretPosition(tankTurret1Pos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                        .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                        .withPositionTransformator(transformedTankPos -> transformedTankPos)
                        .withDetectorConfig(DetectorConfigBuilder.builder()
                              .withDetectorReach(260)
                              .withDetectorAngle(360)
                              .build())
                        .withProjectileType(ProjectileTypes.LASER_BEAM)
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                                    .build())
                              .withSalveSize(2)
                              .withRoundsPerMinute(150)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.builder()
                                          .withDimensionRadius(2)
                                          .withHeightFromBottom(tankTurretHeight)
                                          .build())
                                    .withVelocity(projectileVelocity)
                                    .build())
                              .build())
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(battleTankGunCarriageRadius)
                              .withAmountOfPoints(battleTankGunCarriageRadius)
                              .withCenter(tankTurret2Pos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(battleTankGunHeight)
                        .withGunWidth(battleTankGunWidth)
                        .withTurretPosition(tankTurret2Pos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .build(battleShipHolder))
            .build();
      // Then
      List<TankGridElement> turretGridElements = tankBattleApplication.getTankGridElements();
      assertThat(tankBattleApplication.getTurretGridElements().isEmpty(), is(true));
      assertThat(turretGridElements.size(), is(1));
      TankGridElement tankGridElement = turretGridElements.get(0);
      assertThat(tankGridElement.getPosition(), is(battleShipPos));
      assertThat(tankGridElement.getBelligerentParty(), is(BelligerentPartyConst.GALACTIC_EMPIRE));
      assertThat(TurretCluster.class.isAssignableFrom(tankGridElement.getTurret().getClass()), is(true));
   }

   @Test
   void testBuildTankBattleApplication_WithHumanTankAndTurretImpl() {

      // Given
      Grid grid = mock(Grid.class);
      int tankWidth = 40;
      int tankHeight = 90;

      Position rebelTankPos = Positions.of(450, 600).rotate(80);
      TankHolder rebelTankHolder = new TankHolder();
      double rebelHealth = 0;

      TankDetector tankDetector = mock(TankDetector.class);
      HumanTankEngine humanTankEngine = mock(HumanTankEngine.class);
      double gunWidth = 5;
      GunCarriage gunCarriage = buildGunCarriage(DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM, rebelTankPos, gunWidth);
      HumanControlledTurretStrategyHandler turretStrategyHandler = mock(HumanControlledTurretStrategyHandler.class);

      // When
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(mock(MoveableAdder.class))
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .addTankGridElement(rebelTankHolder, TankBattleApplicationTankBuilder.builder()
                  .withGrid(grid)
                  .withHealth(rebelHealth)
                  .withTankHeight(tankHeight)
                  .withEngineVelocity(25)
                  .withTankTurretHeight(DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM)
                  .withTankPos(rebelTankPos)
                  .withTankWidth(tankWidth)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankStrategy(TankStrategy.HUMAN_CONTROLLED)
                  .withTankEngine(humanTankEngine)
                  .withTankDetector(tankDetector)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE)
                  .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
                  .addTurret(TankBattleApplicationHumanTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withProjectileType(ProjectileTypes.BULLET)
                        .withTurretStrategyHandler(turretStrategyHandler)
                        .withPositionTransformator(pos -> pos.movePositionForward(150))
                        .withGunCarriage(gunCarriage)
                        .build())
                  .build(rebelTankHolder))
            .build();

      // Then
      List<TankGridElement> turretGridElements = tankBattleApplication.getTankGridElements();
      assertThat(tankBattleApplication.getTurretGridElements().isEmpty(), is(true));
      assertThat(turretGridElements.size(), is(1));
      TankGridElement humanTankGridElement = turretGridElements.get(0);
      assertThat(humanTankGridElement.getTankEngine(), is(humanTankEngine));
   }

   @Test
   void testBuildTankBattleApplication_BuildAndDestroyHumanTank() {

      // Given
      TankHolder rebelTankHolder = new TankHolder();
      double rebelHealth = 0;
      Grid grid = mock(Grid.class);

      OnDestroyedCallbackHandler onDestroyedCallbackHandler = mock(OnDestroyedCallbackHandler.class);
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(mock(MoveableAdder.class))
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .addTankGridElement(rebelTankHolder, TankBattleApplicationTankBuilder.builder()
                  .withGrid(grid)
                  .withHealth(rebelHealth)
                  .withTankHeight(40)
                  .withEngineVelocity(25)
                  .withTankTurretHeight(DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM)
                  .withTankPos(Positions.of(450, 600))
                  .withDefaultOnDestructionHandler(onDestroyedCallbackHandler)
                  .withTankWidth(90)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankStrategy(TankStrategy.HUMAN_CONTROLLED)
                  .withTankEngine(mock(HumanTankEngine.class))
                  .withTankDetector(mock(TankDetector.class))
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE)
                  .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
                  .addTurret(TankBattleApplicationHumanTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withProjectileType(ProjectileTypes.BULLET)
                        .withTurretStrategyHandler(mock(HumanControlledTurretStrategyHandler.class))
                        .withPositionTransformator(pos -> pos.movePositionForward(150))
                        .withGunCarriage(buildGunCarriage(DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM, Positions.of(450, 600), 5))
                        .build())
                  .build(rebelTankHolder))
            .build();
      TankGridElement humanTankGridElement = tankBattleApplication.getTankGridElements().get(0);

      // When
      humanTankGridElement.onCollision(mockCollisionProjectiles());

      // Then
      assertThat(humanTankGridElement.isDestroyed(), is(true));
      verify(onDestroyedCallbackHandler).onDestroy();
   }

   private DefaultGunCarriageImpl buildGunCarriage(double tankTurretHeight, Position rebelTankPos, double gunWidth) {
      return DefaultGunCarriageBuilder.builder()
            .withRotationSpeed(5)
            .withGun(DefaultGunBuilder.builder()
                  .withGunProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .build())
                        .withSalveSize(1)
                        .withRoundsPerMinute(900)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.builder()
                                    .withDimensionRadius(3)
                                    .withHeightFromBottom(+tankTurretHeight)
                                    .build())
                              .withVelocity(5)
                              .build())
                        .build())
                  .withGunShape(GunShapeBuilder.builder()
                        .withBarrel(RectangleBuilder.builder()
                              .withHeight(gunWidth)
                              .withWidth(gunWidth)
                              .withCenter(rebelTankPos)
                              .withOrientation(Orientation.HORIZONTAL)
                              .build())
                        .build())
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius(gunWidth * 3d)
                  .withAmountOfPoints(5)
                  .withCenter(rebelTankPos)
                  .build())
            .build();
   }

   @Test
   void testBuildTankBattleApplication_BuildAndDestroyTurretGridElement() {

      // Given
      Grid grid = mock(Grid.class);
      int healthValue = 0;
      LazyGridElement lazyGridElement = new LazyGridElement();
      TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(MoveableAdderBuilder.builder()
                  .withMoveableVelocity(8)
                  .withCounter(200)
                  .withPadding(healthValue)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .build())
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withGrid(grid)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withDestructionHelper(DestructionHelperBuilder.builder()
                        .withDamage(1)
                        .withHealth(healthValue)
                        .withOnDestroyedCallbackHandler(() -> {
                        })
                        .withSelfDestructiveDamage(0)
                        .build())
                  .withDetectorConfig(DetectorConfigBuilder.builder()
                        .withDetectorReach(250)
                        .withDetectorAngle(360)
                        .build())
                  .withProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .build())
                        .withSalveSize(3)
                        .withRoundsPerMinute(200)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.builder()
                                    .withDimensionRadius(3)
                                    .withHeightFromBottom(DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM)
                                    .build())
                              .withVelocity(1)
                              .build())
                        .build())
                  .withGunCarriageShape(CircleBuilder.builder()
                        .withRadius(1)
                        .withAmountOfPoints(5)
                        .withCenter(Positions.of(70, 70))
                        .build())
                  .withGunHeight(1)
                  .withGunWidth(1)
                  .withTurretPosition(Positions.of(70, 70))
                  .withTurretRotationSpeed(1)
                  .build(), lazyGridElement)
            .build();

      // When
      TurretGridElement turret = (TurretGridElement) lazyGridElement.getGridElement();
      turret.onCollision(mockCollisionProjectiles());

      // Then
      assertThat(turret.isDestroyed(), is(true));
   }

   @Test
   void testBuildTankBattleApplication_TurretGridElement() {

      // Given
      Position northTurretPos = Positions.of(70, 70).rotate(-60);
      MirrorGrid grid = MirrorGridBuilder.builder()
            .withMaxX(800)
            .withMaxY(900)
            .withMinX(0)
            .withMinY(0)
            .build();

      double tankTurretHeight = GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
      double tankHeightFromGround = GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
      double turretRotationSpeed = 6;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;
      int projectileVelocity = 50;

      // When
      Orientation gunOrientation = Orientation.HORIZONTAL;
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(MoveableAdderBuilder.builder()
                  .withMoveableVelocity(8)
                  .withCounter(200)
                  .withPadding(0)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .build())
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withGrid(grid)
                  .withGunOrientation(gunOrientation)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withDetectorConfig(DetectorConfigBuilder.builder()
                        .withDetectorReach(250)
                        .withDetectorAngle(360)
                        .build())
                  .withProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .build())
                        .withSalveSize(3)
                        .withRoundsPerMinute(200)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.builder()
                                    .withDimensionRadius(3)
                                    .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                    .build())
                              .withVelocity(projectileVelocity)
                              .build())
                        .build())
                  .withGunCarriageShape(CircleBuilder.builder()
                        .withRadius(gunCarriageRadius)
                        .withAmountOfPoints(gunCarriageRadius)
                        .withCenter(northTurretPos)
                        .build())
                  .withGunHeight(gunHeight)
                  .withGunWidth(gunWidth)
                  .withTurretPosition(northTurretPos)
                  .withTurretRotationSpeed(turretRotationSpeed)
                  .build())
            .build();

      // Then
      List<TurretGridElement> turretGridElements = tankBattleApplication.getTurretGridElements();
      assertThat(tankBattleApplication.getTankGridElements().isEmpty(), is(true));
      assertThat(turretGridElements.size(), is(1));
      TurretGridElement turretGridElement = turretGridElements.get(0);
      assertMuzzleBrake(turretGridElement, false);
      assertThat(turretGridElement.getPosition(), is(northTurretPos));
      assertThat(turretGridElement.getBelligerentParty(), is(BelligerentPartyConst.GALACTIC_EMPIRE));
      TurretShape turretShape = (TurretShape) turretGridElement.getShape();
      assertThat(turretShape.getGunShape().getBarrel().getOrientation(), is(gunOrientation));
   }

   private void assertMuzzleBrake(Turret turret, boolean isMuzzleBrakePresent) {
      assertThat(TurretShape.class.isAssignableFrom(turret.getShape().getClass()), is(true));
      GunShape gunShape = ((TurretShape) turret.getShape()).getGunShape();
      assertThat(gunShape.getMuzzleBreak().isPresent(), is(isMuzzleBrakePresent));
   }

   private List<GridElement> mockCollisionProjectiles() {
      return Collections.singletonList(mockProjectileGridElementent(11));
   }

   private ProjectileGridElement mockProjectileGridElementent(double damage) {
      ProjectileGridElement projectile = mock(ProjectileGridElement.class);
      when(projectile.getDamage()).thenReturn(DamageImpl.of(damage));
      return projectile;
   }
}
