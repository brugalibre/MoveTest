/**
 * 
 */
package com.myownb3.piranha.statemachine.impl.handler.postevasion;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.statemachine.handler.postevasion.PostEvasionStateHandler;

/**
 * @author Dominic
 *
 */
class PostEvasionStateHandlerTest {

    @Test
    void testGetAngle2Turn_PositivButTooSmall() {

	// Given
	int stepWidth = 10;
	double startPosAngle = 135;
	double expectAngle2Turn = 0.0;

	Position moveablePos = Positions.of(5, 5);
	moveablePos.rotate(45);

	PostEvasionStateHandler test = new DefaultPostEvasionStateHandler(stepWidth);

	// When
	double actualAngle2Turn = test.getAngle2Turn(moveablePos, startPosAngle);

	// Then
	assertThat(actualAngle2Turn, is(expectAngle2Turn));
    }

    @Test
    void testGetAngle2Turn_PositivButBigEnough() {

	// Given
	int stepWidth = 10;
	double startPosAngle = 235;
	double expectAngle2Turn = 10;

	Position moveablePos = Positions.of(5, 5);
	moveablePos.rotate(45);

	PostEvasionStateHandler test = new DefaultPostEvasionStateHandler(stepWidth);

	// When
	double actualAngle2Turn = test.getAngle2Turn(moveablePos, startPosAngle);

	// Then
	assertThat(actualAngle2Turn, is(expectAngle2Turn));
    }

    @Test
    void testGetAngle2Turn_NegativButBigEnough() {

	// Given
	int stepWidth = 10;
	double startPosAngle = 90;
	double expectAngle2Turn = -4.5;

	Position moveablePos = Positions.of(5, 5);
	moveablePos.rotate(45);

	PostEvasionStateHandler test = new DefaultPostEvasionStateHandler(stepWidth);

	// When
	double actualAngle2Turn = test.getAngle2Turn(moveablePos, startPosAngle);

	// Then
	assertThat(actualAngle2Turn, is(expectAngle2Turn));
    }

}