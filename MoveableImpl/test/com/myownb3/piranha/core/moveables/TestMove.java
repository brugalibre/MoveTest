/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.DirectionImpl;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

/**
 * @author Dominic
 *
 */
class TestMove {

   @Test
   void testMoveBackwardNTimes() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      int maxMovements = 50;
      Position effectStartPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(0, -maxMovements / Moveable.STEP_WITDH);

      // When
      for (int i = 0; i < maxMovements; i++) {
         moveable.moveBackward();
      }

      // Then
      Position endPosition = moveable.getPosition();

      MatcherAssert.assertThat(effectStartPosition, is(Positions.of(0, 0)));
      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);
   }

   @Test
   void testMoveForwardNegativeValues() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      Executable ex = () -> moveable.moveBackward(-3);
      // Then
      Assertions.assertThrows(IllegalArgumentException.class, ex);
   }

   @Test
   void testMoveForwardNTimes() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      int maxMovements = 50;
      Position effectStartPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(0, maxMovements / Moveable.STEP_WITDH);

      // When
      for (int i = 0; i < maxMovements; i++) {
         moveable.moveForward();
      }

      // Then
      Position endPosition = moveable.getPosition();

      MatcherAssert.assertThat(effectStartPosition, is(Positions.of(0, 0)));
      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);
   }

   ///////////////////////////////////////////////
   // Turn Right //
   ///////////////////////////////////////////////

   @Test
   public void testTurnRight() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();
      Direction[] resultList = new DirectionImpl[] {Directions.O, Directions.S, Directions.W, Directions.N};

      // When
      for (int i = 0; i < resultList.length; i++) {

         moveable.turnRight();

         // Then
         Position endPosition = moveable.getPosition();
         Position expectedEndPosition = Positions.of(Directions.N, 0, 0, 0);
         Direction expectedDirection = resultList[i];

         MatcherAssert.assertThat(endPosition, is(expectedEndPosition));
         MatcherAssert.assertThat(endPosition.getDirection(), is(expectedDirection));
      }
   }

   @Test
   public void testTurnRightTwoTimesAndMoveForwardOnce() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();
      Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
      Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
      expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.O);
      expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);

      // When
      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         moveable.turnRight();
         effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
      }
      moveable.moveForward(10);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(Directions.S, 0, -1, 0);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);

      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         MatcherAssert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
      }
   }

   @Test
   public void testTurnRightThreeTimesAndMoveForwardOnce() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();
      Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
      Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
      expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.O);
      expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);
      expectedPositionToTurnMap.put(Integer.valueOf(2), Directions.W);

      // When
      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         moveable.turnRight();
         effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
      }
      moveable.moveForward(10);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(Directions.W, -1, 0, 0);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);

      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         MatcherAssert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
      }
   }

   ///////////////////////////////////////////////
   // Turn Left //
   ///////////////////////////////////////////////

   @Test
   public void testTurnLeft() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();
      Direction[] resultList = new DirectionImpl[] {Directions.W, Directions.S, Directions.O, Directions.N};

      // When
      for (int i = 0; i < resultList.length; i++) {
         moveable.turnLeft();

         // Then
         Position endPosition = moveable.getPosition();
         Position expectedEndPosition = Positions.of(Directions.N, 0, 0, 0);
         Direction expectedDirection = resultList[i];

         MatcherAssert.assertThat(endPosition, is(expectedEndPosition));
         MatcherAssert.assertThat(endPosition.getDirection(), is(expectedDirection));
      }
   }

   ///////////////////////////////////////////////
   // Move Backwards & Turn Left //
   ///////////////////////////////////////////////

   @Test
   public void testTurnLeftOnceTimesAndMoveBackwardOnce() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      moveable.turnLeft();
      moveable.moveBackward(10);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(Directions.W, 1, 0, 0);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);
   }

   @Test
   public void testTurnLeftTwoTimesAndMoveBackwardOnce() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();
      Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
      Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
      expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.W);
      expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);

      // When
      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         moveable.turnLeft();
         effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
      }
      moveable.moveBackward(10);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(Directions.S, 0, 1, 0);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);

      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         MatcherAssert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
      }
   }

   @Test
   public void testTurnLeftThreeTimesAndMoveBackwardOnce() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withGrid(GridBuilder.builder()
                  .build())
            .build();
      Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
      Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
      expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.W);
      expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);
      expectedPositionToTurnMap.put(Integer.valueOf(2), Directions.O);

      // When
      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         moveable.turnLeft();
         effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
      }
      moveable.moveBackward(10);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(Directions.O, -1, 0, 0);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);

      for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
         MatcherAssert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
      }
   }

   @Test
   public void testMoveBackward() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();
      DirectionImpl expectedDirection = Directions.N;

      // When
      moveable.moveBackward(10);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(0, -1);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);
      MatcherAssert.assertThat(endPosition.getDirection(), is(expectedDirection));
   }

   @Test
   public void testTurn45DegreeAndMoveForward() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      moveable.makeTurn(45);
      moveable.moveForward(100);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(null, -7.071, 7.071, 0);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);
   }

   @Test
   public void testTurn_ZeroDegree() {

      // Given
      int angle = 0;
      Position pos = spy(Positions.of(0, 0));
      MoveablePostActionHandler spyHandler = spy(new MoveablePostActionHandlerTest());
      DefaultGrid grid = GridBuilder.builder()
            .build();
      Moveable moveable = new SimpleTestMoveable(grid, pos, spyHandler);

      // When
      moveable.makeTurn(angle);

      // Then
      verify(spyHandler, never()).handlePostConditions(moveable);// No PostActions while creating a moveable!

      // this method
      verify(pos, never()).rotate(angle);
   }

   @Test
   public void testTestEmptyPostActionHandler() {

      // Given
      int angle = 0;
      Position pos = spy(Positions.of(0, 0));
      Moveable moveable = new SimpleTestMoveable(GridBuilder.builder()
            .build(), pos, res -> {
            });

      // When
      moveable.makeTurn(angle);

      // Then
      verify(pos, never()).rotate(angle);
   }

   @Test
   public void testTurnXDegreeAndMoveForward() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withGrid(GridBuilder.builder()
                  .build())
            .build();
      Position expectedStopover1 = Positions.of(null, -3.536, 3.536, 0);
      Position expectedStopover2 = Positions.of(null, -6.433, 4.312, 0);
      Position expectedStopover3 = Positions.of(null, -5.398, 0.448, 0);

      // When
      moveable.makeTurn(45); // 135; x:-0.7071 ; y:+0.7071
      moveable.moveForward(50); // x:-3.54 ; y: +3.54

      Position effectStopover1 = moveable.getPosition();

      moveable.makeTurn(30); // 165; x:-0.965925 ; y: 0.25882
      moveable.moveForward(30); // x: -2.8978; y: 0.7764
      Position effectStopover2 = moveable.getPosition();

      moveable.makeTurn(-60); // 105; x: -0.258819; y:0.965925
      moveable.moveBackward(40); // x:1.035276 ; y=3.8637
      Position effectStopover3 = moveable.getPosition();

      // Then
      com.myownb3.piranha.test.Assert.assertThatPosition(effectStopover1, is(expectedStopover1), 3);
      com.myownb3.piranha.test.Assert.assertThatPosition(effectStopover2, is(expectedStopover2), 3);
      com.myownb3.piranha.test.Assert.assertThatPosition(effectStopover3, is(expectedStopover3), 3);
   }

   @Test
   public void testTurnMinus30DegreeAndMoveBackward() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      // When
      moveable.makeTurn(-30);
      moveable.moveBackward(100);

      // Then
      Position endPosition = moveable.getPosition();
      Position expectedEndPosition = Positions.of(null, -5, -8.66, 0);

      com.myownb3.piranha.test.Assert.assertThatPosition(endPosition, is(expectedEndPosition), 3);
   }

   private static class MoveablePostActionHandlerTest implements MoveablePostActionHandler {

      @Override
      public void handlePostConditions(Moveable moveable) {
         // Nothing to do
      }
   }

   private static class SimpleTestMoveable extends AbstractMoveable {

      public SimpleTestMoveable(Grid grid, Position position, MoveablePostActionHandler handler) {
         super(grid, handler, PositionShapeBuilder.builder()
               .withPosition(position)
               .build(), getDefaultDimensionInfo(1), 1);
      }
   }
}
