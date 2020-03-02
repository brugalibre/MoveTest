package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder.EndPointMoveableBuilder;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;

class EndPointMoveableImplTest {

    @Test
    void testIsDone() {
	Position endPos = Positions.of(0, 1);
	Position pos = Positions.of(0, 0.9);
	Grid grid = GridBuilder.builder().build();
	Detector detector = mock(Detector.class);
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

	// Given
	EndPointMoveable moveable = EndPointMoveableBuilder.builder()
		.withGrid(grid)
		.withPosition(pos)
		.withHandler(new EvasionStateMachine(detector, endPos, config))//
		.widthEndPosition(endPos)//
		.build();
	// When
	MoveResult moveResult = moveable.moveForward2EndPos();

	// Then
	assertThat(moveResult.isDone(), is(true));
    }

    @Test
    void testIsDone_BecauseAlreadyToFar() {
	Position endPos = Positions.of(0, 1);
	Position pos = Positions.of(0, 0.9);
	Grid grid = GridBuilder.builder().build();
	Detector detector = mock(Detector.class);
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

	// Given
	EndPointMoveable moveable = EndPointMoveableBuilder.builder ()
		.withGrid(grid)
		.withPosition(pos)
		.withHandler(new EvasionStateMachine(detector, endPos, config))//
		.widthEndPosition(endPos)//
		.withMovingIncrement(4)//
		.build();
	// When
	MoveResult moveResult = moveable.moveForward2EndPos();

	// Then
	assertThat(moveResult.isDone(), is(true));
    }

}
