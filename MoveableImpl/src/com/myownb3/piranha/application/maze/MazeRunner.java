package com.myownb3.piranha.application.maze;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.application.MoveableApplication;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorCluster;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.maze.Maze;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.PostEvasionStateHandler4Maze;

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
               .withPassingDistance(25)
               .withPostEvasionReturnAngle(4)
               .withDetectorConfig(DetectorConfigBuilder.builder()
                     .withDetectorReach(detectorReach)
                     .withEvasionDistance(evasionDistance)
                     .withDetectorAngle(75)
                     .withEvasionAngle(45)
                     .withEvasionAngleInc(1)
                     .build())
               .build();
         return this;
      }

      public MazeRunnerBuilder withTrippleDetectorCluster(DetectorConfig centerDetectorConfig, DetectorConfig sideDetectorConfig) {
         detectorCluster = TrippleDetectorClusterBuilder.buildDefaultDetectorCluster(centerDetectorConfig, sideDetectorConfig);
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
               .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
               .withGrid(maze.getGrid())
               .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                     .withDetector(detectorCluster)
                     .withGrid(maze.getGrid())
                     .withPostEvasionStateHandler(new PostEvasionStateHandler4Maze())
                     .withEvasionStateMachineConfig(config)
                     .build())
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
