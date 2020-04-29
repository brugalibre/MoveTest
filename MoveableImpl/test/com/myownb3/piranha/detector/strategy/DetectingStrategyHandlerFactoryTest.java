package com.myownb3.piranha.detector.strategy;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.detector.cluster.DetectingStrategyHandler;
import com.myownb3.piranha.detector.cluster.tripple.SupportiveFlanksDetectingStrategyHandler;

class DetectingStrategyHandlerFactoryTest {

   @Test
   void testGetHandler_UnknownOne() {
      // Given
      DetectingStrategy detectingStrategy = DetectingStrategy.NONE;

      // When
      Executable ex = () -> {
         DetectingStrategyHandlerFactory.getHandler(detectingStrategy);
      };
      // Then
      assertThrows(IllegalArgumentException.class, ex);
   }

   @Test
   void testGetHandler_SupportiveFlanks() {
      // Given
      DetectingStrategy detectingStrategy = DetectingStrategy.SUPPORTIVE_FLANKS;

      // When
      DetectingStrategyHandler detectingStrategyHandler = DetectingStrategyHandlerFactory.getHandler(detectingStrategy);
      // Then
      assertThat(detectingStrategyHandler, instanceOf(SupportiveFlanksDetectingStrategyHandler.class));
   }
}
