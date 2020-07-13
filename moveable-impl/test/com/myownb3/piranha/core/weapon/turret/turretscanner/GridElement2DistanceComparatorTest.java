package com.myownb3.piranha.core.weapon.turret.turretscanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class GridElement2DistanceComparatorTest {

   @Test
   void testCompare() {

      // Given
      List<GridElement> targetGridElements = new LinkedList<>();
      Position gridElement1Pos = Positions.of(15, 15);
      Position gridElement2Pos = Positions.of(10, 10);
      Position posToComapreTo = Positions.of(0, 0);
      targetGridElements.add(SimpleGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElement1Pos)
                  .build())
            .build());

      targetGridElements.add(SimpleGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElement2Pos)
                  .build())
            .build());

      // When
      Collections.sort(targetGridElements, new GridElement2DistanceComparator(posToComapreTo));

      // Then
      assertThat(targetGridElements.get(0).getPosition(), is(gridElement2Pos));
   }

}
