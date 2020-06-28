package com.myownb3.piranha.application.random;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.piranha.application.MoveableApplication;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl;
import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositionGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;

public class RandomMoveableWithEndPositionRunner implements MoveableApplication {
   private MoveableController moveableController;
   private TrippleDetectorCluster detectorCluster;
   private EvasionStateMachineConfig config;
   private List<GridElement> allGridElements;
   private Grid grid;

   private RandomMoveableWithEndPositionRunner() {
      allGridElements = new ArrayList<>();
   }

   @Override
   public List<Position> run() {
      moveableController.leadMoveable();
      return moveableController.getMoveable().getPositionHistory();
   }

   public Grid getGrid() {
      return grid;
   }

   @Override
   public List<GridElement> getAllGridElements() {
      return Collections.unmodifiableList(allGridElements);
   }

   public EvasionStateMachineConfig getConfig() {
      return config;
   }

   public MoveableController getMoveableController() {
      return moveableController;
   }

   public TrippleDetectorCluster getDetectorCluster() {
      return detectorCluster;
   }

   public static class RandomRunnerWithEndPositionsBuilder {

      private Grid grid;
      private List<GridElement> gridElements;
      private MoveableController moveableController;
      private List<EndPosition> endPositions;
      private Position startPosition;
      private int circleRadius;

      private Detector detector;
      private EvasionStateMachineConfig config;
      private int movingIncrement;
      private DetectorConfig sideDetectorConfig;

      private RandomRunnerWithEndPositionsBuilder() {
         movingIncrement = 2;
         gridElements = new ArrayList<>();
      }

      public RandomRunnerWithEndPositionsBuilder withCircleRadius(int circleRadius) {
         this.circleRadius = circleRadius;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withMovingIncrement(int movingIncrement) {
         this.movingIncrement = movingIncrement;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withStartPos(Position startPosition) {
         this.startPosition = startPosition;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withRandomEndPositions(int amountOfEndPos) {
         requireNonNull(grid, "We neeeda Grid before we can add End-Positions!");
         withEndPositions(getEndPosList(circleRadius, circleRadius, grid.getDimension(), amountOfEndPos));
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withEndPositions(List<EndPosition> endPositions) {
         this.endPositions = endPositions;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withMoveableController(MoveableController moveableController) {
         this.moveableController = moveableController;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withEvasionStateMachineConfig(EvasionStateMachineConfig config) {
         this.config = config;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withSideDetectorConfig(DetectorConfig sideDetectorConfig) {
         this.sideDetectorConfig = sideDetectorConfig;
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withDefaultDetectorCluster() {
         detector = TrippleDetectorClusterBuilder.buildDefaultDetectorCluster(DetectorConfigImpl.of(config), sideDetectorConfig);
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withMoveableController(PostMoveForwardHandler postMoveFowardHandler) {
         requireNonNull(grid, "We neeeda Grid before we can build the MoveableController!");
         this.moveableController = MoveableControllerBuilder.builder()
               .withStrategie(MovingStrategy.FORWARD)
               .withEndPositions(endPositions)
               .withPostMoveForwardHandler(postMoveFowardHandler)
               .withEndPointMoveable()
               .withGrid(grid)
               .withStartPosition(startPosition)
               .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                     .withDetector(detector)
                     .withGrid(grid)
                     .withEvasionStateMachineConfig(config)
                     .build())
               .withShape(buildCircle(4, startPosition))
               .withMovingIncrement(movingIncrement)
               .buildAndReturnParentBuilder()
               .build();
         return this;
      }

      public RandomRunnerWithEndPositionsBuilder withRandomMoveableObstacles(int amount) {
         requireNonNull(grid, "Build a Grid bevore adding Moveables!");

         for (int i = 0; i < amount; i++) {
            Dimension dimension = grid.getDimension();
            Position randomPosition = Positions.getRandomPosition(dimension, circleRadius, circleRadius);
            Obstacle obstacle = MoveableObstacleBuilder.builder()
                  .withGrid(grid)
                  .withPosition(randomPosition)
                  .withShape(CircleBuilder.builder()
                        .withRadius(circleRadius)
                        .withAmountOfPoints(20)
                        .withCenter(randomPosition)
                        .build())
                  .withVelocity(6)
                  .build();

            gridElements.add(obstacle);
         }
         return this;
      }

      public RandomMoveableWithEndPositionRunner build() {
         RandomMoveableWithEndPositionRunner endPositionRunner = new RandomMoveableWithEndPositionRunner();
         endPositionRunner.moveableController = moveableController;
         if (detector instanceof TrippleDetectorCluster) {
            endPositionRunner.detectorCluster = (TrippleDetectorCluster) detector;
         }
         endPositionRunner.config = config;
         endPositionRunner.grid = grid;
         endPositionRunner.allGridElements.addAll(endPositions.stream()
               .map(endPos -> new EndPositionGridElement(grid, endPos, CircleBuilder.builder()
                     .withRadius(circleRadius)
                     .withAmountOfPoints(5)
                     .withCenter(endPos)
                     .build()))
               .collect(Collectors.toList()));
         endPositionRunner.allGridElements.addAll(gridElements);
         return endPositionRunner;
      }

      public static RandomRunnerWithEndPositionsBuilder builder() {
         return new RandomRunnerWithEndPositionsBuilder();
      }

   }

   private static List<EndPosition> getEndPosList(int height, int width, Dimension dimension, int amountOfEndPos) {
      List<EndPosition> endPosList = new ArrayList<>(amountOfEndPos);
      for (int i = 0; i < amountOfEndPos; i++) {
         endPosList.add(EndPositions.of(Positions.getRandomPosition(dimension, height, width), 5));
      }
      return endPosList;
   }

   private static Shape buildCircle(int width, Position pos) {
      return CircleBuilder.builder()
            .withRadius(width)
            .withAmountOfPoints(30)
            .withCenter(pos)
            .build();//
   }
}
