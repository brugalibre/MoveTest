package com.myownb3.piranha.core.moveables.controller.forwardstrategy.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.position.EndPosition;

class IncrementalForwardStrategyHandlerTest {

   @Test
   void testEvalNextEndPos_ReachTheEndOfTheList() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5);
      EndPosition endPos2 = EndPositions.of(10, 10);
      List<EndPosition> endPosList = Arrays.asList(endPos1, endPos2);

      // When
      EndPosition actualNextEndPos = IncrementalForwardStrategyHandler.evalNextEndPos(endPosList, endPos2);

      // Then
      assertThat(actualNextEndPos, is(endPos1));
   }

   @Test
   void testEvalNextEndPos_EmptyList() {

      // Given
      EndPosition endPos1 = EndPositions.of(5, 5);
      List<EndPosition> endPosList = Collections.emptyList();

      // When
      Executable exe = () -> {
         IncrementalForwardStrategyHandler.evalNextEndPos(endPosList, endPos1);
      };

      // Then
      assertThrows(IllegalArgumentException.class, exe);
   }

}
