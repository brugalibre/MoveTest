/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;

/**
 * @author Dominic
 *
 */
class SimpleGridElementTest {

    /**
     * Test method for {@link com.myownb3.piranha.grid.gridelement.SimpleGridElement#toString()}.
     */
    @Test
    void testToString() {
	// Given
	Position position = Positions.of(1, 1);
	Grid grid = new DefaultGrid(5,5);
	SimpleGridElement element = new SimpleGridElement(grid, position );
	String expectedToString = "Position: Direction: 'Cardinal-Direction:N, Rotation: 90.0', X-Axis: '1.0', Y-Axis: '1.0'\nMax x:'5, Min x:'0; Max y:'5, Min y:'0";
	
	// When
	String toString = element.toString();

	// Then
	assertThat(toString, is(expectedToString));
    }

}
