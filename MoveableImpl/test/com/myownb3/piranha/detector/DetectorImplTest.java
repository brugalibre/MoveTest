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
import com.myownb3.piranha.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.grid.gridelement.GridElement;
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

	GridElement g1 = new AbstractGridElement(grid, positionG1);
	GridElement g2 = new AbstractGridElement(grid, positionG2);
	GridElement g3 = new AbstractGridElement(grid, positionG3);
	List<GridElement> gridElements = Arrays.asList(g1, g2, g3);
	Collections.shuffle(gridElements);
	
	// When
	Optional<GridElement> actualNearestGridElem = detector.getNearestEvasionGridElement(position, gridElements);

	// Then
	Assert.assertThat(actualNearestGridElem.get(), is(g2));
    }
}
