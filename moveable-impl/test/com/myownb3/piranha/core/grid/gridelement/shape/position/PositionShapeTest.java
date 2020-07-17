package com.myownb3.piranha.core.grid.gridelement.shape.position;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class PositionShapeTest {

   @Test
   void testBuildPath4Detection() {
      // Given
      Position gridElemPos = Positions.of(5, 5);
      PositionShape positionShape = PositionShapeBuilder.builder()
            .withPosition(gridElemPos)
            .build();

      // When
      List<Position> path4Detection = positionShape.buildPath4Detection();

      // Then
      assertThat(path4Detection, is(singletonList(positionShape.getPosition())));
   }
}
