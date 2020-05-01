package com.myownb3.piranha.application.maze;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.application.MoveableApplication;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.maze.Maze;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.moveables.MovingStrategy;
import com.myownb3.piranha.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigBuilder;

public class MazeRunner implements MoveableApplication {
   private Maze maze;
   private MoveableController moveableController;
   private TrippleDetectorCluster detectorCluster;
   private EvasionStateMachineConfig config;

   private MazeRunner() {
      // private
   }

   @Override
   public List<Position> run() {
      moveableController.leadMoveable();
      return moveableController.getMoveable().getPositionHistory();
   }

   public Grid getGrid() {
      return maze.getGrid();
   }

   @Override
   public List<GridElement> getAllGridElements() {
      return Collections.unmodifiableList(maze.getAllMazeGridElements());
   }

   public Maze getMaze() {
      return maze;
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

   public static class MazeRunnerBuilder {

      private Maze maze;
      private MoveableController moveableController;
      private Position startPosition;

      private TrippleDetectorCluster detectorCluster;
      private EvasionStateMachineConfig config;
      private int movingIncrement;

      private MazeRunnerBuilder() {
         movingIncrement = 2;
      }

      public MazeRunnerBuilder withStartPos(Position startPosition) {
         this.startPosition = startPosition;
         return this;
      }

      public MazeRunnerBuilder withMovingIncrement(int movingIncrement) {
         this.movingIncrement = movingIncrement;
         return this;
      }

      public MazeRunnerBuilder withMoveableController(MoveableController moveableController) {
         this.moveableController = moveableController;
         return this;
      }

      public MazeRunnerBuilder withMaze(Maze maze) {
         this.maze = maze;
         return this;
      }

      public MazeRunnerBuilder withEvasionStateMachineConfig(int detectorReach, int evasionDistance) {
         this.config = EvasionStateMachineConfigBuilder.builder()
               .withReturningAngleIncMultiplier(1)
               .withOrientationAngle(1)
               .withReturningMinDistance(0.06)
               .withReturningAngleMargin(0.7d)
               .withDetectorReach(detectorReach)
               .withEvasionDistance(evasionDistance)
               .withPassingDistance(25)
               .withDetectorAngle(75)
               .withEvasionAngle(45)
               .withEvasionAngleInc(1)
               .withPostEvasionReturnAngle(4)
               .build();
         return this;
      }

      public MazeRunnerBuilder withTrippleDetectorCluster(int sideDetectorReach, int sideDetectorEvasionDistance) {
         detectorCluster = TrippleDetectorClusterBuilder.buildDefaultDetectorCluster(config,
               EvasionStateMachineConfigBuilder.builder()
                     .withReturningAngleIncMultiplier(1)
                     .withOrientationAngle(1)
                     .withReturningMinDistance(0.06)
                     .withReturningAngleMargin(0.7d)
                     .withDetectorReach(sideDetectorReach)
                     .withEvasionDistance(sideDetectorEvasionDistance)
                     .withPassingDistance(25)
                     .withDetectorAngle(60)
                     .withEvasionAngle(45)
                     .withEvasionAngleInc(1)
                     .withPostEvasionReturnAngle(4)
                     .build());
         return this;
      }

      public MazeRunnerBuilder withMoveableController(PostMoveForwardHandler postMoveFowardHandler) {
         requireNonNull(maze, "We neeeda Maze before we can build the MoveableController!");
         MovingStrategy strategy = maze.getEndPositions().isEmpty() ? MovingStrategy.FORWARD_WITHOUT_END_POS : MovingStrategy.FORWARD;
         this.moveableController = MoveableControllerBuilder.builder()
               .withStrategie(strategy)
               .withEndPositions(maze.getEndPositions())
               .withPostMoveForwardHandler(postMoveFowardHandler)
               .withEndPointMoveable()
               .withGrid(maze.getGrid())
               .withStartPosition(startPosition)
               .withHandler(new EvasionStateMachine(detectorCluster, config))
               .withShape(CircleBuilder.builder()
                     .withRadius(4)
                     .withAmountOfPoints(4)
                     .withCenter(startPosition)
                     .build())
               .withMovingIncrement(movingIncrement)
               .buildAndReturnParentBuilder()
               .build();
         return this;
      }

      public MazeRunner build() {
         MazeRunner mazeRunner = new MazeRunner();
         mazeRunner.maze = maze;
         mazeRunner.moveableController = moveableController;
         mazeRunner.detectorCluster = detectorCluster;
         mazeRunner.config = config;
         return mazeRunner;
      }

      public static MazeRunnerBuilder builder() {
         return new MazeRunnerBuilder();
      }
   }
}
