package com.myownb3.piranha.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.grid.Grid;

class MoveableObstacleImplTest {

    @Test
    void testMoveableObstacleImpl() {

	// Given
	MoveableObstacleImpl moveable = new MoveableObstacleImpl(Mockito.mock(Grid.class),
		Mockito.mock(Position.class));

	// When

	boolean isObstacle = Obstacle.class.isAssignableFrom(moveable.getClass());
	
	// Then
	assertThat(isObstacle, is(true));
    }
}
