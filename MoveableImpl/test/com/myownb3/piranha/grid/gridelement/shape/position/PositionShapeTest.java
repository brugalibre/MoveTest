package com.myownb3.piranha.grid.gridelement.shape.position;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;

class PositionShapeTest {

   @Test
   void testBuildPath4Detection() {
      // Given
      Position gridElemPos = Positions.of(5, 5);
      PositionShape positionShape = new PositionShape(gridElemPos);

      // When
      List<Position> path4Detection = positionShape.buildPath4Detection();

      // Then
      assertThat(path4Detection, is(singletonList(positionShape.getPosition())));
   }
}
