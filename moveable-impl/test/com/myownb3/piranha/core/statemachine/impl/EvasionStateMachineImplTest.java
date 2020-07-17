/**
 * 
 */
package com.myownb3.piranha.core.statemachine.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class EvasionStateMachineImplTest {

   @Test
   void testHandleEvasionState_NoRegisteredStateHandler() {

      // Given
      EvasionStateMachineConfig config = mock(EvasionStateMachineConfig.class);
      Detector detector = mock(Detector.class);
      EvasionStateMachineImpl evasionStateMachine = EvasionStateMachineBuilder.builder()
            .withGrid(mock(Grid.class))
            .withDetector(detector)
            .withEvasionStateMachineConfig(config)
            .build();

      // When
      Executable ex = () -> {
         evasionStateMachine.getHandler4State(EvasionStates.NONE);
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   /**
    * Test method for
    * {@link com.myownb3.piranha.core.statemachine.impl.handler.com.myownb3.piranha.moveables.statemachine.impl.EvasionStateMachine#handleEvasion4CurrentState(com.myownb3.piranha.core.grid.Grid, com.myownb3.piranha.core.moveables.Moveable)}.
    */
   @Test
   void test_HandleEvasion4UnknownState() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 8, 8, 45, 11.25);
      EvasionStateMachineImpl evasionStateMachine = EvasionStateMachineBuilder.builder()
            .withGrid(grid)
            .withDetector(DetectorBuilder.builder()
                  .withDetectorReach(8)
                  .withDetectorAngle(45)
                  .withAngleInc(11.25)
                  .build())
            .withEvasionStateMachineConfig(config)
            .build();
      evasionStateMachine.evasionState = EvasionStates.NONE;

      // When
      Executable ex = () -> {
         evasionStateMachine.handlePostConditions(moveable);
      };

      // Then
      assumeThat(EvasionStates.NONE.nextState(), is(EvasionStates.NONE));
      assertThrows(IllegalStateException.class, ex);
   }
}
