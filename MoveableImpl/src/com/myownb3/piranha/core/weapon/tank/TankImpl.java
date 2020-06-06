package com.myownb3.piranha.core.weapon.tank;

import static java.util.Objects.nonNull;

import java.util.List;

import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

public class TankImpl implements Tank {

   private Turret turret;
   private TankEngine tankEngine;
   private TankShape tankShape;

   private TankImpl(Turret turret, TankEngine tankEngine, TankShape tankShape) {
      this.turret = turret;
      this.tankEngine = tankEngine;
      this.tankShape = tankShape;
   }

   @Override
   public void autodetect() {
      turret.autodetect();
      if (isNotShooting()) {
         tankEngine.moveForward();
      }
   }

   private boolean isNotShooting() {
      return turret.getTurretStatus() != TurretState.SHOOTING;
   }

   @Override
   public Turret getTurret() {
      return turret;
   }

   @Override
   public TankEngine getTankEngine() {
      return tankEngine;
   }

   @Override
   public TankShape getShape() {
      return tankShape;
   }

   @Override
   public Position getPosition() {
      return tankShape.getCenter();
   }

   public static final class TankBuilder {

      private Turret turret;
      private Shape tankHull;
      private Grid grid;
      private int movingIncrement;
      private List<EndPosition> endPositions;
      private TankEngine tankEngine;

      private TankBuilder() {
         movingIncrement = 1;
      }

      public TankBuilder withTurret(Turret turret) {
         this.turret = turret;
         return this;
      }

      public TankBuilder withHull(Shape tankHull) {
         this.tankHull = tankHull;
         return this;
      }

      public TankBuilder withTankEngine(TankEngine tankEngine) {
         this.tankEngine = tankEngine;
         return this;
      }

      public TankBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TankBuilder withEngineVelocity(int movingIncrement) {
         this.movingIncrement = movingIncrement;
         return this;
      }

      public TankBuilder withEndPositions(List<EndPosition> endPositions) {
         this.endPositions = endPositions;
         return this;
      }

      public Tank build() {
         TankShape tankShape = buildTankShape();

         TankHolder tankHolder = new TankHolder();
         TankEngine tankEngine = buildNewOrGetExistingEngine(tankShape, this.tankEngine);
         TankImpl tankImpl = new TankImpl(turret, tankEngine, tankShape);
         return tankHolder
               .setAndReturnTank(tankImpl);
      }

      private TankShape buildTankShape() {
         return TankShapeBuilder.builder()
               .withHull(tankHull)
               .withTurretShape(turret.getShape())
               .build();
      }

      private TankEngine buildNewOrGetExistingEngine(TankShape tankShape, TankEngine tankEngine) {
         if (nonNull(tankEngine)) {
            return tankEngine;
         }
         return TankEngineBuilder.builder()
               .withMoveableController(MoveableControllerBuilder.builder()
                     .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                     .withEndPositions(endPositions)
                     .withEndPointMoveable()
                     .withGrid(grid)
                     .withStartPosition(tankShape.getCenter())
                     .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                           .withDetector(DetectorBuilder.builder()
                                 .build())
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
                           .build())
                     .withShape(tankShape)
                     .withMovingIncrement(movingIncrement)
                     .buildAndReturnParentBuilder()
                     .withPostMoveForwardHandler((m) -> {
                     })
                     .build())
               .build();
      }

      public static TankBuilder builder() {
         return new TankBuilder();
      }
   }
}
