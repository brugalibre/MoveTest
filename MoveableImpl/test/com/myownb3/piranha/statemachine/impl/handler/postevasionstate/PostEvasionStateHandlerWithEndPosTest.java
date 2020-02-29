/**
 * 
 */
package com.myownb3.piranha.statemachine.impl.handler.postevasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jscience.mathematics.vector.Float64Vector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class PostEvasionStateHandlerWithEndPosTest {

    @Test
    public void testHandlePostEvasion_AngleCorrectionNotNecessary() {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withStepWidth(10)//
		.withEndPos(Positions.of(10, 10))//
		.withPositionBeforeEvasion(Positions.of(9, 9))//
		.withEvasionStateHandler()//
		.build()//
		.withEventStateInput();

	// When
	CommonEventStateResult commonEventStateResult = tcb.handler.handle(tcb.evenStateInput);

	// Then
	assertThat(commonEventStateResult.getNextState(), is(EvasionStates.POST_EVASION.nextState()));
	verify(tcb.moveable, never()).makeTurnWithoutPostConditions(anyDouble());
	assertThat(tcb.handler.state, is(PostEvasionStates.POST_EVASION));
    }

    @Test
    public void testHandlePostEvasion_AngleCorrectionNecessary() {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withStepWidth(10)//
		.withEndPos(Positions.of(10, 10))//
		.withPositionBeforeEvasion(Positions.of(9, 9))//
		.withEvasionStateHandler()//
		.withHandlerAngle(15).withSignum(-1).build()//
		.withEventStateInput();

	// When
	CommonEventStateResult commonEventStateResult = tcb.handler.handle(tcb.evenStateInput);

	// Then
	assertThat(commonEventStateResult.getNextState(), is(EvasionStates.POST_EVASION));
	assertThat(tcb.handler.state, is(PostEvasionStates.POST_EVASION));
	verify(tcb.moveable).makeTurnWithoutPostConditions(
		Mockito.eq(tcb.handler.testSignum * PostEvasionStateHandlerWithEndPos.MIN_ANGLE_TO_TURN));
	verify(tcb.helper, times(3)).checkSurrounding(eq(tcb.grid), eq(tcb.moveable));
	verify(tcb.helper, times(2)).check4Evasion(eq(tcb.grid), eq(tcb.moveable));
    }

    @Test
    public void testHandlePostEvasion_AngleCorrectionNecessary_WithEvasionOnSecondCall() {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withHelper(spy(new TestDetectableMoveableHelper())).withStepWidth(10)//
		.withEndPos(Positions.of(10, 10))//
		.withPositionBeforeEvasion(Positions.of(9, 9))//
		.withEvasionStateHandler()//
		.withHandlerAngle(10).withSignum(-1).build()//
		.withEventStateInput();

	// When
	CommonEventStateResult firstCEventStateResult = tcb.handler.handle(tcb.evenStateInput);

	// during the second call we're getting an evasion
	((TestDetectableMoveableHelper) tcb.helper).isCheck4EvasionTrue = true;
	tcb.handler.handle(tcb.evenStateInput);

	// Then
	assertThat(firstCEventStateResult.getNextState(), is(EvasionStates.POST_EVASION));
	assertThat(tcb.handler.state, is(PostEvasionStates.POST_EVASION));
	verify(tcb.moveable, times(2))
		.makeTurnWithoutPostConditions(Mockito.eq(tcb.handler.testSignum * tcb.handler.angle));
	verify(tcb.moveable).makeTurnWithoutPostConditions(Mockito.eq(tcb.handler.testSignum * -tcb.handler.angle / 2));
	verify(tcb.helper, times(6)).checkSurrounding(eq(tcb.grid), eq(tcb.moveable));
	verify(tcb.helper, times(4)).check4Evasion(eq(tcb.grid), eq(tcb.moveable));
    }

    private static class TestCaseBuilder {

	private PostEvasionEventStateInput evenStateInput;
	private double stepWidth;
	private Position endPos;
	private Position positionBeforeEvasion;
	private DetectableMoveableHelper helper;
	private Grid grid;
	private Moveable moveable;
	private TestPostEvasionStateHandler handler;

	public TestCaseBuilder() {
	    helper = mock(DetectableMoveableHelper.class);
	    grid = mock(Grid.class);
	    moveable = spyMoveable();
	}

	private TestCaseBuilder withHelper(DetectableMoveableHelper helper) {
	    this.helper = helper;
	    return this;
	}

	private TestCaseBuilder withStepWidth(double stepWidth) {
	    this.stepWidth = stepWidth;
	    return this;
	}

	private TestCaseBuilder withEndPos(Position endPos) {
	    this.endPos = endPos;
	    return this;
	}

	private TestCaseBuilder withPositionBeforeEvasion(Position positionBeforeEvasion) {
	    this.positionBeforeEvasion = positionBeforeEvasion;
	    return this;
	}

	private TestCaseBuilder withEventStateInput() {
	    evenStateInput = PostEvasionEventStateInput.of(helper, grid, moveable, positionBeforeEvasion);
	    return this;
	}

	private PostEvasionStateHandlerBuilder withEvasionStateHandler() {
	    PostEvasionStateHandlerBuilder builder = new PostEvasionStateHandlerBuilder();
	    builder.withPostEvasionStateHandler(endPos, stepWidth);
	    return builder;
	}

	private Moveable spyMoveable() {
	    Moveable moveable = spy(Moveable.class);
	    Mockito.when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
	    return moveable;
	}

	private class PostEvasionStateHandlerBuilder {

	    private TestPostEvasionStateHandler handler;

	    private PostEvasionStateHandlerBuilder withPostEvasionStateHandler(Position endPos, double stepWidth) {
		handler = new TestPostEvasionStateHandler(endPos, stepWidth);
		return this;
	    }

	    private PostEvasionStateHandlerBuilder withHandlerAngle(double angle) {
		handler.angle = angle;
		return this;
	    }

	    private PostEvasionStateHandlerBuilder withSignum(int signum) {
		handler.testSignum = signum;
		return this;
	    }

	    private TestCaseBuilder build() {
		TestCaseBuilder.this.handler = this.handler;
		return TestCaseBuilder.this;
	    }
	}
    }

    private static class TestDetectableMoveableHelper extends DetectableMoveableHelper {

	private boolean isCheck4EvasionTrue;

	public TestDetectableMoveableHelper() {
	    super(mock(Detector.class));
	    isCheck4EvasionTrue = false;
	}

	@Override
	public boolean check4Evasion(Grid grid, GridElement moveable) {
	    return isCheck4EvasionTrue;
	}
    }

    private static class TestPostEvasionStateHandler extends PostEvasionStateHandlerWithEndPos {

	private Float64Vector endPosLine;
	private int testSignum;
	private double angle;

	public TestPostEvasionStateHandler(Position endPos, double stepWidth) {
	    super(stepWidth);
	    endPosLine = Float64Vector.valueOf(1, 2, 0);
	}

	@Override
	protected Float64Vector getEndPosLine(Direction posBeforeEvasionDirection, Position endPos) {
	    return endPosLine;
	}

	@Override
	protected int calcSignumWithDistance(Position moveablePos, Position positionBeforeEvasion,
		Float64Vector endPosLine, double testTurnAngle) {
	    return testSignum;
	}

	@Override
	protected double calcAngle(Position moveablePos, Float64Vector endPosLine) {
	    return angle;
	}
    }
}
