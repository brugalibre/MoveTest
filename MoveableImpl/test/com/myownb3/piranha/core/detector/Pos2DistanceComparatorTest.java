package com.myownb3.piranha.core.detector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;

class Pos2DistanceComparatorTest {

   @Test
   void testComparingOrder() {

      // Given
      Position pos1 = Positions.of(10, 10);
      Position pos2 = Positions.of(1, 1);
      List<Position> positions2Compare = Arrays.asList(pos1, pos2);
      Position posToComapreTo = Positions.of(0, 0);

      // When
      Collections.sort(positions2Compare, new Pos2DistanceComparator(posToComapreTo));

      // Then
      assertThat(positions2Compare.get(0), is(pos2));
   }

   @Test
   void testComparingOrder2() {

      // Given
      Position pos1 = Positions.of(10, 10);
      Position pos2 = Positions.of(1, 1);
      Position pos3 = Positions.of(4, 4);
      List<Position> positions2Compare = Arrays.asList(pos1, pos2, pos3);
      Position posToComapreTo = Positions.of(3, 3);

      // When
      Collections.sort(positions2Compare, new Pos2DistanceComparator(posToComapreTo));

      // Then
      assertThat(positions2Compare.get(0), is(pos3));
   }
}
