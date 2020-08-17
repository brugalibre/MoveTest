package com.myownb3.piranha.application.battle.impl;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.application.battle.util.MoveableAdder;
import com.myownb3.piranha.audio.impl.AudioClipCloser;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngine;
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
import com.myownb3.piranha.core.grid.gridelement.lazy.GenericLazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl.RectanglePathBuilderImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl.RectanglePathBuilderImpl.RectangleSides;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio.EngineAudioBuilder;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public class TankBattleApplicationImpl implements TankBattleApplication {

   private MoveableAdder moveableAdder;
   private Grid grid;
   private List<TankGridElement> tankGridElements;
   private List<TurretGridElement> turretGridElements;
   private EvasionStateMachineConfig evasionStateMachineConfig;
   private int cycleCounter;

   public TankBattleApplicationImpl(Grid grid, MoveableAdder moveableAdder, List<TankGridElement> tankGridElements,
         List<TurretGridElement> turretGridElements, EvasionStateMachineConfig evasionStateMachineConfig) {
      this.grid = grid;
      this.evasionStateMachineConfig = evasionStateMachineConfig;
      this.moveableAdder = moveableAdder;
      this.tankGridElements = tankGridElements;
      this.turretGridElements = turretGridElements;
      this.cycleCounter = 0;
   }

   @Override
   public void run() {
      cycleCounter++;
      callAutodetect();
      if (moveableAdder.isCycleOver(cycleCounter)) {
         checkAndAddNewMoveables();
         cycleCounter = 0;
      }
   }

   private void callAutodetect() {
      grid.getAllGridElements(null).parallelStream()
            .filter(isGridElementAlive())
            .filter(AutoDetectable.class::isInstance)
            .map(AutoDetectable.class::cast)
            .forEach(AutoDetectable::autodetect);
   }

   private void checkAndAddNewMoveables() {
      moveableAdder.check4NewMoveables2Add(grid, evasionStateMachineConfig);
   }

   private static Predicate<? super GridElement> isGridElementAlive() {
      return DestructionHelper::isNotDestroyed;
   }

   @Override
   public List<TankGridElement> getTankGridElements() {
      return tankGridElements;
   }

   @Override
   public List<TurretGridElement> getTurretGridElements() {
      return turretGridElements;
   }

   @Override
   public void prepare() {
      grid.prepare();
      WorkerThreadFactory.INSTANCE.restart();
      AudioClipCloser.INSTANCE.start();
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
      private double tankHealth;
      private TankDetector tankDetector;
      private TankEngine tankEngine;
      private OnDestroyedCallbackHandler onDestroyedCallbackHandler;

      private TankBattleApplicationTankBuilder() {
         this.turrets = new ArrayList<>();
         this.tankHealth = Integer.MAX_VALUE;
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

      public TankBattleApplicationTankBuilder withEvasionStateMachineConfig(EvasionStateMachineConfig evasionStateMachineConfig) {
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

      public TankBattleApplicationTankBuilder withHealth(double tankHealth) {
         this.tankHealth = tankHealth;
         return this;
      }

      public TankBattleApplicationTankBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankEngine(TankEngine tankEngine) {
         this.tankEngine = tankEngine;
         return this;
      }

      public TankBattleApplicationTankBuilder withTankDetector(TankDetector tankDetector) {
         this.tankDetector = tankDetector;
         return this;
      }

      public TankBattleApplicationTankBuilder withDefaultOnDestructionHandler(OnDestroyedCallbackHandler onDestroyedCallbackHandler) {
         this.onDestroyedCallbackHandler = onDestroyedCallbackHandler;
         return this;
      }

      public TankBattleApplicationTankBuilder build(TankHolder tankHolder) {
         this.tank = TankBuilder.builder()
               .withBelligerentParty(belligerentParty)
               .withHealth(tankHealth)
               .withOnDestroyedCallbackHandler(onDestroyedCallbackHandler)
               .withTankEngine(getTankEngine(tankHolder))
               .withTankDetector(getTankDetector(tankHolder))
               .withTurret(getTankTurret())
               .withHull(RectangleBuilder.builder()
                     .withRectanglePathBuilder(new RectanglePathBuilderImpl(20, RectangleSides.FRONT_AND_BACK))
                     .withCenter(tankPos)
                     .withHeight(tankHeight)
                     .withWidth(tankWidth)
                     .withOrientation(Orientation.HORIZONTAL)
                     .build())
               .withTankStrategy(tankStrategy)
               .build();
         return this;
      }

      private TankEngine getTankEngine(TankHolder tankHolder) {
         if (nonNull(tankEngine)) {
            return tankEngine;
         }
         return TankEngineBuilder.builder()
               .withVelocity(engineVelocity)
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
               .build();
      }

      private TankDetector getTankDetector(TankHolder tankHolder) {
         if (nonNull(tankDetector)) {
            return tankDetector;
         }
         return TankDetectorBuilder.builder()
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
               .build();
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
      private MoveableAdder moveableAdder;
      private Grid grid;
      private List<TankGridElement> tankGridElements;
      private List<TurretGridElement> turretGridElements;
      private EvasionStateMachineConfig evasionStateMachineConfig;

      private TankBattleApplicationBuilder() {
         this.tankGridElements = new ArrayList<>();
         this.turretGridElements = new ArrayList<>();
      }

      public TankBattleApplicationBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TankBattleApplicationBuilder withMoveableAdder(MoveableAdder moveableAdder) {
         this.moveableAdder = moveableAdder;
         return this;
      }

      public TankBattleApplicationBuilder withEvasionStateMachineConfig(EvasionStateMachineConfig evasionStateMachineConfig) {
         this.evasionStateMachineConfig = evasionStateMachineConfig;
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

      public TankBattleApplicationBuilder addTurretGridElement(Turret turret, GenericLazyGridElement<TurretGridElement> lazyGridElement) {
         TurretGridElement turretGridElement = TurretGridElementBuilder.builder()
               .withGrid(grid)
               .withHeightFromBottom(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM)
               .withTurret(turret)
               .build();
         turretGridElements.add(turretGridElement);
         lazyGridElement.setGridElement(turretGridElement);
         return this;
      }

      public TankBattleApplicationBuilder addTankGridElement(TankHolder tankHolder,
            TankBattleApplicationTankBuilder tankBattleApplicationTankBuilder) {
         requireNonNull(grid);
         requireNonNull(tankBattleApplicationTankBuilder.tank);
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

      public TankBattleApplicationImpl build() {
         return new TankBattleApplicationImpl(grid, moveableAdder, tankGridElements, turretGridElements, evasionStateMachineConfig);
      }

      public static TankBattleApplicationBuilder builder() {
         return new TankBattleApplicationBuilder();
      }
   }
}
