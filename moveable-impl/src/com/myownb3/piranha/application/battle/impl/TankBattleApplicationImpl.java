package com.myownb3.piranha.application.battle.impl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.TurretClusterImpl.TurretClusterBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio.EngineAudioBuilder;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;

public class TankBattleApplicationImpl implements TankBattleApplication {

   private List<TankGridElement> tankGridElements;
   private List<TurretGridElement> turretGridElements;
   private List<GridElement> allGridElements;

   public TankBattleApplicationImpl(List<TankGridElement> tankGridElements, List<TurretGridElement> turretGridElements) {
      this.tankGridElements = tankGridElements;
      this.turretGridElements = turretGridElements;
   }

   @Override
   public void run() {}

   @Override
   public List<GridElement> getAllGridElements() {
      return allGridElements;
   }

   @Override
   public List<TankGridElement> getTankGridElements() {
      return tankGridElements;
   }

   @Override
   public List<TurretGridElement> getTurretGridElements() {
      return turretGridElements;
   }

   public static class TankBattleApplicationTankBuilder {

      private List<Turret> turrets;
      private Grid grid;

      private List<EndPosition> endPositions;
      private Position tankPos;
      private double tankHeight;
      private double tankWidth;
      private TankStrategy tankStrategy;
      private String tankEngineAudioResource;
      private EvasionStateMachineConfig evasionStateMachineConfig;
      private double tankTurretHeight;
      private Tank tank;
      private int engineVelocity;
      private BelligerentParty belligerentParty;

      private TankBattleApplicationTankBuilder() {
         this.turrets = new ArrayList<>();
      }

      public TankBattleApplicationTankBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TankBattleApplicationTankBuilder withEndPositions(List<EndPosition> endPositions) {
         this.endPositions = endPositions;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankHeight(double tankHeight) {
         this.tankHeight = tankHeight;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankTurretHeight(double tankTurretHeight) {
         this.tankTurretHeight = tankTurretHeight;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankWidth(double tankWidth) {
         this.tankWidth = tankWidth;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankPos(Position tankPos) {
         this.tankPos = tankPos;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankStrategy(TankStrategy tankStrategy) {
         this.tankStrategy = tankStrategy;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankEngineAudioResource(String tankEngineAudioResource) {
         this.tankEngineAudioResource = tankEngineAudioResource;
         return this;
      }

      public TankBattleApplicationTankBuilder withEvasionStateMachine(EvasionStateMachineConfig evasionStateMachineConfig) {
         this.evasionStateMachineConfig = evasionStateMachineConfig;
         return this;
      }

      public TankBattleApplicationTankBuilder addTurret(Turret turret) {
         this.turrets.add(turret);
         return this;
      }

      public TankBattleApplicationTankBuilder withEngineVelocity(int engineVelocity) {
         this.engineVelocity = engineVelocity;
         return this;
      }

      public TankBattleApplicationTankBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public TankBattleApplicationTankBuilder build(TankHolder tankHolder) {
         this.tank = TankBuilder.builder()
               .withBelligerentParty(belligerentParty)
               .withTankEngine(TankEngineBuilder.builder()
                     .withVelocity(12)
                     .withDefaultEngineStateHandler()
                     .withEngineAudio(EngineAudioBuilder.builder()
                           .withDefaultAudio()
                           .withEngineMoveAudio(AudioClipBuilder.builder()
                                 .withRestartRunningAudio(false)
                                 .withAudioResource(tankEngineAudioResource)
                                 .build())
                           .build())
                     .withMoveableController(MoveableControllerBuilder.builder()
                           .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                           .withEndPositions(endPositions)
                           .withLazyMoveable(() -> tankHolder.getTankGridElement())
                           .build())
                     .build())
               .withTankDetector(TankDetectorBuilder.builder()
                     .withTankGridElement(() -> tankHolder.getTankGridElement())
                     .withGrid(grid)
                     .withDetector(TrippleDetectorClusterBuilder.builder()
                           .withCenterDetector(DetectorBuilder.builder()
                                 .withAngleInc(1)
                                 .withDetectorAngle(90)
                                 .withDetectorReach(400)
                                 .withEvasionAngle(90)
                                 .withEvasionDistance(400)
                                 .build())
                           .withLeftSideDetector(DetectorBuilder.builder()
                                 .withAngleInc(1)
                                 .withDetectorAngle(90)
                                 .withDetectorReach(400)
                                 .withEvasionAngle(90)
                                 .withEvasionDistance(400)
                                 .build(), 90)
                           .withRightSideDetector(DetectorBuilder.builder()
                                 .withAngleInc(1)
                                 .withDetectorAngle(90)
                                 .withDetectorReach(400)
                                 .withEvasionAngle(90)
                                 .withEvasionDistance(400)
                                 .build(), 90)
                           .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS_WITH_DETECTION)
                           .withAutoDetectionStrategyHandler()
                           .build())
                     .build())
               .withTurret(getTankTurret())
               .withHull(RectangleBuilder.builder()
                     .withCenter(tankPos)
                     .withHeight(tankHeight)
                     .withWidth(tankWidth)
                     .withOrientation(Orientation.HORIZONTAL)
                     .build())
               .withTankStrategy(tankStrategy)
               .build();
         return this;
      }

      private Turret getTankTurret() {
         if (turrets.size() == 1) {
            return turrets.get(0);
         }
         return buildTurretClusterBuilder();
      }

      private Turret buildTurretClusterBuilder() {
         TurretClusterBuilder turretClusterBuilder = TurretClusterBuilder.builder()
               .withPosition(tankPos);
         for (Turret turret : turrets) {
            turretClusterBuilder.addTurret(turret);
         }
         return turretClusterBuilder.build();
      }

      public static TankBattleApplicationTankBuilder builder() {
         return new TankBattleApplicationTankBuilder();
      }
   }

   public static class TankBattleApplicationBuilder {
      private List<TankGridElement> tankGridElements;
      private List<TurretGridElement> turretGridElements;
      private Grid grid;

      private TankBattleApplicationBuilder() {
         this.tankGridElements = new ArrayList<>();
         this.turretGridElements = new ArrayList<>();
      }

      public TankBattleApplicationImpl build() {
         return new TankBattleApplicationImpl(tankGridElements, turretGridElements);
      }

      public static TankBattleApplicationBuilder builder() {
         return new TankBattleApplicationBuilder();
      }

      public TankBattleApplicationBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TankBattleApplicationBuilder addTurretGridElement(Turret turret) {
         turretGridElements.add(TurretGridElementBuilder.builder()
               .withGrid(grid)
               .withHeightFromBottom(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM)
               .withTurret(turret)
               .build());
         return this;
      }

      public TankBattleApplicationBuilder addTankGridElement(TankHolder tankHolder,
            TankBattleApplicationTankBuilder tankBattleApplicationTankBuilder) {
         requireNonNull(grid);
         TankGridElement tankGridElement = TankGridElementBuilder.builder()
               .withGrid(grid)
               .withEngineVelocity(12)
               .withTankheightFromBottom(tankBattleApplicationTankBuilder.tankTurretHeight)
               .withTurretHeightFromBottom(tankBattleApplicationTankBuilder.tankTurretHeight)
               .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                     .withGrid(grid)
                     .withDetector(DetectorBuilder.builder()
                           .build())
                     .withEvasionStateMachineConfig(tankBattleApplicationTankBuilder.evasionStateMachineConfig)
                     .build())
               .withTank(tankBattleApplicationTankBuilder.tank)
               .build();
         tankGridElements.add(tankGridElement);
         tankHolder.setAndReturnTank(tankGridElement);
         tankHolder.setTankGridElement(tankGridElement);
         return this;
      }
   }
}
