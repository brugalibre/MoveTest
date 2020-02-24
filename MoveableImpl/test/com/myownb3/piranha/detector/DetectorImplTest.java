package com.myownb3.piranha.detector;

import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;

class DetectorImplTest {

    @Test
    void testGetNearestEvasionGridElement() {

	// Given
	DetectorImpl detector = new DetectorImpl();
	Grid grid = new DefaultGrid(100, 100);
	Position position = Positions.of(0, 0);
	Position positionG1 = Positions.of(50, 50);
	Position positionG2 = Positions.of(49, 49);
	Position positionG3 = Positions.of(90, 90);

	Obstacle g1 = new ObstacleImpl(grid, positionG1);
	Obstacle g2 = new ObstacleImpl(grid, positionG2);
	Obstacle g3 = new ObstacleImpl(grid, positionG3);
	List<Avoidable> obstacles = Arrays.asList(g1, g2, g3);
	Collections.shuffle(obstacles);
	
	// When
	Optional<Avoidable> actualNearestGridElem = detector.getNearestEvasionAvoidable(position, obstacles);

	// Then
	Assert.assertThat(actualNearestGridElem.get(), is(g2));
    }
}
