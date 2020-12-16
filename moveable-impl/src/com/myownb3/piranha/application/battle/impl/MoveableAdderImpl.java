package com.myownb3.piranha.application.battle.impl;

import static com.myownb3.piranha.util.MathUtil.getRandom;
import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.myownb3.piranha.application.Application;
import com.myownb3.piranha.application.battle.MoveableAdder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.shape.TIEFighterShapeImpl.TIEFighterShapeBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.lazy.LazyEndPoinMoveable;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController.AutoMoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.util.MathUtil;

public class MoveableAdderImpl implements MoveableAdder {

   private int amountOfNonMoveables;
   private int amountOfMoveables;
   private int moveableVelocity;
   private double gridElementRadius;
   private int counter;
   private int padding;
   private BelligerentParty belligerentParty;
   private int cycleCounter;

   private MoveableAdderImpl(int amountOfNonMoveables, int amountOfMoveables, int moveableVelocity, int counter, int padding,
         double gridElementRadius,
         BelligerentParty belligerentParty) {
      this.amountOfNonMoveables = amountOfNonMoveables;
      this.amountOfMoveables = amountOfMoveables;
      this.moveableVelocity = moveableVelocity;
      this.gridElementRadius = gridElementRadius;
      this.counter = counter;
      this.padding = padding;
      this.cycleCounter = 0;
      this.belligerentParty = belligerentParty;
   }

   /**
    * Adds new {@link GridElement} if necessary
    * 
    * @param grid
    *        the {@link Grid}
    * @param evasionStateMachineConfig
    *        the {@link EvasionStateMachineConfig} for added {@link Moveable}s
    * @param padding
    *        the padding
    * @return all new created {@link GridElement}
    */
   @Override
   public List<GridElement> check4NewMoveables2Add(Grid grid, EvasionStateMachineConfig evasionStateMachineConfig) {
      List<GridElement> addedGridElements = new ArrayList<>();
      double moveableCounter = countMoveables(grid);
      double simpleGridElementCounter = countNonMoveables(grid);
      if (moveableCounter < amountOfMoveables) {
         addedGridElements.add(buildNewMoveable(grid, evasionStateMachineConfig, padding));
      }
      if (simpleGridElementCounter < amountOfNonMoveables) {
         addedGridElements.add(buildObstacle(grid, padding));
      }
      resetCycleCounter();
      return addedGridElements;
   }

   private void resetCycleCounter() {
      cycleCounter = 0;
   }

   /**
    * Handles a new cycle on a {@link Application}
    * Return <code>true</code> if the current cycle is over, which means that the internal <code>cycleCounter</code> is greater or equal
    * then the counter size defined by this {@link MoveableAdderImpl} or <code>false</code> if not
    * This internal <code>cycleCounter</code> is reseted to <code>0</code> if this method returns <code>true</code>
    * 
    * 
    * @return <code>true</code> if the current cycle is over and <code>false</code> if not
    */
   @Override
   public boolean isCycleDone() {
      return cycleCounter >= counter;
   }

   /**
    * Increments the cycle counter of this {@link MoveableAdder}
    */
   @Override
   public void incrementCounter() {
      cycleCounter++;
   }

   private double countNonMoveables(Grid grid) {
      return grid.getAllGridElements(null).stream()
            .filter(isMoveable().negate())
            .filter(isNotProjectile())
            .filter(isObstacle())
            .filter(isGridElementAlive())
            .filter(isSameBelligerentParty())
            .count();
   }

   private double countMoveables(Grid grid) {
      return grid.getAllGridElements(null).stream()
            .filter(isMoveable())
            .filter(isNotProjectile())
            .filter(isNotTank())
            .filter(isGridElementAlive())
            .filter(isSameBelligerentParty())
            .count();
   }

   private Predicate<? super GridElement> isSameBelligerentParty() {
      return gridElement -> ((Belligerent) gridElement).getBelligerentParty() == belligerentParty;
   }

   private Moveable buildNewMoveable(Grid grid, EvasionStateMachineConfig evasionStateMachineConfig, int padding) {
      int maxY = grid.getDimension().getHeight();
      int maxX = grid.getDimension().getWidth();
      double yCordinate = getRandomYCoordinate(padding, gridElementRadius, maxY);
      double xCordinate = getRandomXCoordinate(padding, gridElementRadius, maxX);
      double angle2Rotate = -getRandom(90) + 15;
      Position gridElementPos = Positions.of(xCordinate, yCordinate)
            .rotate(angle2Rotate);

      LazyEndPoinMoveable lazyEndPoinMoveable = new LazyEndPoinMoveable();
      List<EndPosition> endPosList = getEndPosList(15, gridElementPos, gridElementRadius);
      AutoMoveableController autoMoveableController = AutoMoveableControllerBuilder.builder()
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(3)
                  .withHealth(500)
                  .withSelfDestructiveDamage(moveableVelocity)
                  .withDestroyedAudioClip(AudioClipBuilder.builder()
                        .withAudioResource(AudioConstants.EXPLOSION_SOUND)
                        .build())
                  .withOnDestroyedCallbackHandler(() -> {
                     grid.remove(lazyEndPoinMoveable.getGridElement());
                  })
                  .build())
            .withBelligerentParty(belligerentParty)
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(gridElementRadius))
            .withMoveableController(MoveableControllerBuilder.builder()
                  .withEndPositions(endPosList)
                  .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                  .withLazyMoveable(() -> lazyEndPoinMoveable.getGridElement())
                  .build())
            .withGrid(grid)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(evasionStateMachineConfig)
                  .build())
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(gridElementRadius)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(moveableVelocity)
            .build();
      lazyEndPoinMoveable.setGridElement(autoMoveableController);
      return autoMoveableController;
   }

   private Obstacle buildObstacle(Grid grid, int padding) {
      int maxY = grid.getDimension().getHeight() + grid.getDimension().getY();
      int maxX = grid.getDimension().getWidth() + grid.getDimension().getX();
      double yCordinate = getRandomYCoordinate(padding, gridElementRadius, maxY);
      double xCordinate = getRandomXCoordinate(padding, gridElementRadius, maxX);
      double angle2Rotate = -getRandom(90) + 15;
      Position gridElementPos = Positions.of(xCordinate, yCordinate).rotate(angle2Rotate);
      return ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .withBelligerentParty(belligerentParty)
            .build();
   }

   private double getRandomYCoordinate(double padding, double gridElementRadius, int maxY) {
      double betweenMinAndMaxY = getRandom(maxY) + padding;
      return min(betweenMinAndMaxY, maxY - 3d * gridElementRadius);
   }

   private double getRandomXCoordinate(int padding, double gridElementRadius, int maxX) {
      double betweenMinAndMaxX = getRandom(maxX) + padding;
      return min(betweenMinAndMaxX, maxX - 3d * gridElementRadius);
   }

   private static List<EndPosition> getEndPosList(int amountOfEndPos, Position gridElementPos, double gridElementRadius) {
      double distance = gridElementRadius * (MathUtil.getRandom(8) + 4);
      Position position = gridElementPos.movePositionForward4Distance(distance);
      List<EndPosition> endPosList = new ArrayList<>(amountOfEndPos);
      endPosList.add(EndPositions.of(position, 5));
      for (int i = 0; i < amountOfEndPos; i++) {
         double degree = MathUtil.getRandom(360);
         position = position.rotate(degree)
               .movePositionForward4Distance(distance);
         endPosList.add(EndPositions.of(position, 10));
      }
      return endPosList;
   }

   private static Predicate<? super GridElement> isMoveable() {
      return moveable -> moveable instanceof Moveable
            && !(moveable instanceof ProjectileGridElement);
   }

   private Predicate<? super GridElement> isObstacle() {
      return moveable -> (moveable instanceof Obstacle);
   }

   private static Predicate<? super GridElement> isNotProjectile() {
      return moveable -> !(moveable instanceof ProjectileGridElement);
   }

   private Predicate<? super GridElement> isNotTank() {
      return moveable -> !(moveable instanceof TankGridElement);
   }

   private static Predicate<? super GridElement> isGridElementAlive() {
      return DestructionHelper::isNotDestroyed;
   }

   public static class MoveableAdderBuilder {
      private int amountOfNonMoveables;
      private int amountOfMoveables;
      private int moveableVelocity;
      private double gridElementRadius;
      private int counter;
      private int padding;
      private BelligerentParty belligerentParty;

      private MoveableAdderBuilder() {
         this.amountOfNonMoveables = 5;
         this.gridElementRadius = 9;
         this.amountOfMoveables = 5;
         this.moveableVelocity = 20;
         this.counter = 50;
      }

      public MoveableAdderBuilder withAmountOfNonMoveables(int amountOfNonMoveables) {
         this.amountOfNonMoveables = amountOfNonMoveables;
         return this;
      }

      public MoveableAdderBuilder withAmountOfMoveables(int amountOfMoveables) {
         this.amountOfMoveables = amountOfMoveables;
         return this;
      }

      public MoveableAdderBuilder withMoveableVelocity(int moveableVelocity) {
         this.moveableVelocity = moveableVelocity;
         return this;
      }

      public MoveableAdderBuilder withGridElementRadius(int gridElementRadius) {
         this.gridElementRadius = gridElementRadius;
         return this;
      }

      public MoveableAdderBuilder withCounter(int counter) {
         this.counter = counter;
         return this;
      }

      public MoveableAdderBuilder withPadding(int padding) {
         this.padding = padding;
         return this;
      }

      public MoveableAdderBuilder withBelligerentParty(BelligerentParty belligerent) {
         this.belligerentParty = belligerent;
         return this;
      }

      public MoveableAdderImpl build() {
         requireNonNull(belligerentParty);
         return new MoveableAdderImpl(amountOfNonMoveables, amountOfMoveables, moveableVelocity, counter, padding, gridElementRadius,
               belligerentParty);
      }

      public static MoveableAdderBuilder builder() {
         return new MoveableAdderBuilder();
      }
   }
}
