/**
 * 
 */
package com.myownb3.piranha.statemachine.impl.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.postevasion.input.PostEvasionEventStateInput;

/**
 * @author Dominic
 *
 */
class PostEvasionStateHandlerTest {

    @Test
    public void testSchnappi() {

	// Given
	double stepWidth = 10;
	Position endPos = Positions.of(10, 10);
	Position positionBeforeEvasion = Positions.of(9, 9);
	DetectableMoveableHelper helper = spy(DetectableMoveableHelper.class);
	Grid grid = mock(Grid.class);
	Moveable moveable = mock(Moveable.class);

	PostEvasionStateHandler handler = new PostEvasionStateHandler(endPos, stepWidth );

	PostEvasionEventStateInput evenStateInput = PostEvasionEventStateInput.of(helper, grid, moveable, positionBeforeEvasion);

	// When
	handler.handle(evenStateInput);
	
	// Then
    }

}
