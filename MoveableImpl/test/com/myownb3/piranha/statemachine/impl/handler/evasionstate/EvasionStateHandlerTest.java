package com.myownb3.piranha.statemachine.impl.handler.evasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.EvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.output.EvasionStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

class EvasionStateHandlerTest {

    @Test
    void testHandleEvasionState_NonEvasionAtAll() {

	// Given
	Grid grid = mock(Grid.class);
	double expectedAngle = 0.0d;
	Detector detector = mockDetector(expectedAngle);
	Moveable moveable = MoveableBuilder.builder(grid).withHandler((a, b) -> {
	}).build();

	DetectableMoveableHelper helper = new DummyDetectableMoveableHelper(detector);

	EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper);
	EvasionStateHandler test = new EvasionStateHandler();

	// When
	EvasionStateResult evasionStateResult = test.handle(evenStateInput);

	// Then
	assertThat(evasionStateResult.getAvoidAngle(), is(expectedAngle));
	assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION.nextState()));
    }

    @Test
    void testHandleEvasionState_EvasionButAvoidingAngleIsZero() {

	// Given
	Grid grid = mock(Grid.class);
	double expectedAngle = 0.0d;
	Detector detector = spy(Detector.class);
	Moveable moveable = spyMoveable();

	DetectableMoveableHelper helper = new OneTimeDetectableMoveableHelper(detector);

	EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper);
	EvasionStateHandler test = new EvasionStateHandler();

	// When
	EvasionStateResult evasionStateResult = test.handle(evenStateInput);

	// Then
	assertThat(evasionStateResult.getAvoidAngle(), is(expectedAngle));
	assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION.nextState()));
	verify(moveable, never()).makeTurnWithoutPostConditions(Mockito.anyDouble());
	verify(detector).getEvasionAngleRelative2(any());
    }

    @Test
    void testHandleEvasionState_NotEvasionAnymoreAfterFirstTurn() {

	// Given
	Grid grid = mock(Grid.class);
	double expectedAngle = 10;
	Detector detector = mockDetector(expectedAngle);
	Moveable moveable = spyMoveable();

	DetectableMoveableHelper helper = spy(new OneTimeDetectableMoveableHelper(detector));

	EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper);
	EvasionStateHandler test = new EvasionStateHandler();

	// When
	EvasionStateResult evasionStateResult = test.handle(evenStateInput);

	// Then
	assertThat(evasionStateResult.getAvoidAngle(), is(expectedAngle));
	assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION.nextState()));
	verify(moveable, times(2)).makeTurnWithoutPostConditions(Mockito.anyDouble());
	verify(helper).checkSurrounding(eq(grid), eq(moveable));
	verify(helper, times(2)).check4Evasion(eq(grid), eq(moveable));
    }

    @Test
    void testHandleEvasionState_StillEvasionAfterFirstTurn() {

	// Given
	Grid grid = mock(Grid.class);
	double expectedAngle = 10;
	Detector detector = mockDetector(expectedAngle);
	Moveable moveable = spyMoveable();

	DetectableMoveableHelper helper = spy(new AlwaysEvasionDetectableMoveableHelper(detector));

	EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper);
	EvasionStateHandler test = new EvasionStateHandler();

	// When
	EvasionStateResult evasionStateResult = test.handle(evenStateInput);

	// Then
	assertThat(evasionStateResult.getAvoidAngle(), is(expectedAngle));
	assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION));
	verify(moveable).makeTurnWithoutPostConditions(Mockito.anyDouble());
	verify(helper).checkSurrounding(eq(grid), eq(moveable));
	verify(helper, times(2)).check4Evasion(eq(grid), eq(moveable));
    }

    private static Detector mockDetector(Double expectedAngle) {
	Detector detector = mock(Detector.class);
	when(detector.getEvasionAngleRelative2(any())).thenReturn(expectedAngle);
	return detector;
    }

    private Moveable spyMoveable() {
	Moveable moveable = spy(Moveable.class);
	Mockito.when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
	return moveable;
    }

    private static class AlwaysEvasionDetectableMoveableHelper extends DetectableMoveableHelper {
	public AlwaysEvasionDetectableMoveableHelper(Detector detector) {
	    super(detector);
	}

	@Override
	public boolean check4Evasion(Grid grid, GridElement moveable) {
	    return true;
	}

	@Override
	public void checkSurrounding(Grid grid, Moveable moveable) {
	}
    }

    private static class DummyDetectableMoveableHelper extends DetectableMoveableHelper {
	public DummyDetectableMoveableHelper(Detector detector) {
	    super(detector);
	}

	@Override
	public boolean check4Evasion(Grid grid, GridElement moveable) {
	    return false;
	}

	@Override
	public void checkSurrounding(Grid grid, Moveable moveable) {
	}
    }

    private static class OneTimeDetectableMoveableHelper extends DetectableMoveableHelper {
	private boolean hasAllreadyChecked;

	public OneTimeDetectableMoveableHelper(Detector detector) {
	    super(detector);
	    hasAllreadyChecked = false;
	}

	@Override
	public boolean check4Evasion(Grid grid, GridElement moveable) {
	    if (!hasAllreadyChecked) {
		hasAllreadyChecked = true;
		return true;
	    }
	    return false;
	}

	@Override
	public void checkSurrounding(Grid grid, Moveable moveable) {
	}
    }
}
